package com.swpp.footprinter.domain.trace.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.swpp.footprinter.common.Km_PER_LATLNG_DEGREE
import com.swpp.footprinter.common.PLACE_FIND_METER
import com.swpp.footprinter.common.PLACE_GRID_METER
import com.swpp.footprinter.common.TIME_GRID_SEC
import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.common.utils.ImageUrlUtil
import com.swpp.footprinter.common.utils.stringToDate8601
import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.photo.dto.PhotoInitialTraceResponse
import com.swpp.footprinter.domain.photo.repository.PhotoRepository
import com.swpp.footprinter.domain.place.dto.PlaceInitialTraceResponse
import com.swpp.footprinter.domain.place.service.externalAPI.KakaoAPIService
import com.swpp.footprinter.domain.footprint.dto.FootprintInitialTraceResponse
import com.swpp.footprinter.domain.trace.dto.TraceRequest
import com.swpp.footprinter.domain.trace.dto.TraceResponse
import com.swpp.footprinter.domain.trace.model.Trace
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.repository.UserRepository
import com.swpp.footprinter.domain.footprint.service.FootprintService
import com.swpp.footprinter.domain.tag.TAG_CODE
import com.swpp.footprinter.domain.tag.dto.TagResponse
import com.swpp.footprinter.domain.trace.dto.TraceDetailResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.math.sqrt

interface TraceService {
    fun getAllTraces(): List<TraceResponse>
    fun createTrace(traceRequest: TraceRequest)
    fun getTraceById(traceId: Long): TraceDetailResponse
    fun deleteTraceById(traceId: Long)

    fun createInitialTraceBasedOnPhotoIdListGiven(photoIds: List<String>): List<FootprintInitialTraceResponse> // List<Pair<Place, List<Photo>>>
    fun getTraceByDate(date: String): TraceDetailResponse?
}

@Service
class TraceServiceImpl(
    private val traceRepo: TraceRepository,
    private val footprintService: FootprintService,
    private val userRepo: UserRepository,
    private val photoRepo: PhotoRepository,
    private val kakaoAPIService: KakaoAPIService,
    private val imageUrlUtil: ImageUrlUtil,

    @Value("\${cloud.aws.s3.bucket-name}")
    private val bucketName: String
) : TraceService {
    override fun getAllTraces(): List<TraceResponse> {
        return traceRepo.findAll().filter { it.owner != userRepo.findByIdOrNull(1)!! }.map { trace -> trace.toResponse() } // TODO: 현재 user로 넣기
    }

    @Transactional
    override fun createTrace(traceRequest: TraceRequest) {
        val newTrace = Trace(
            traceTitle = traceRequest.title!!,
            traceDate = traceRequest.date!!,
            owner = userRepo.findByIdOrNull(1)!!, // TODO: 현재 user로 넣기
            footprints = mutableSetOf()
        )
        traceRepo.save(newTrace)

        // TODO: 여러 날의 footprint가 들어온 경우 handle
        traceRequest.footprintList!!.forEach {
            // Create new footprints
            val footprint = footprintService.createFootprintAndReturn(it, newTrace)

            // Update newTrace
            newTrace.footprints.add(footprint)
        }
    }

    override fun getTraceById(traceId: Long): TraceDetailResponse {
        val trace = traceRepo.findByIdOrNull(traceId) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        return trace.toDetailResponse().apply {
            footprints?.forEach { fp ->
                fp.photos.forEach { p ->
                    p.imageUrl = imageUrlUtil.getImageURLfromImagePath(p.imagePath)
                }
            }
        }
    }

    override fun deleteTraceById(traceId: Long) {
        traceRepo.deleteById(traceId) // TODO: Authentication
    }

    override fun createInitialTraceBasedOnPhotoIdListGiven(photoIds: List<String>): List<FootprintInitialTraceResponse> {
        val photoEntityList: List<Photo> = photoIds.map {
            photoRepo.findByImagePath(it) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        }

        val initialTraceDTOList = groupPhotosWithLocationAndTimeAndReturnInitialTraceDTOList(photoEntityList)

        addRecomendedPlaceToInitialTraceDTOList(initialTraceDTOList, radius = PLACE_FIND_METER)

        return initialTraceDTOList
    }

    override fun getTraceByDate(date: String): TraceDetailResponse? {
        // TODO: 현재 유저 입력
        return traceRepo
            .findTracesByTraceDate(date)
            .lastOrNull()
            ?.toDetailResponse()
            ?.apply {
                footprints?.forEach { fp ->
                    fp.photos.forEach { p ->
                        p.imageUrl = imageUrlUtil.getImageURLfromImagePath(p.imagePath)
                    }
                }
                footprints = footprints?.sortedBy { stringToDate8601(it.startTime).time }
            }
    }

    /**
     * Assume GPS error is 30m, and 1 degree of lat/lng is 1112km.
     * => Assume there is same place within 0.000027 degree of lat/lng.
     */
    // TODO: Improve algorithm
    fun isNearEnough(photo: PhotoInitialTraceResponse, latitude: Double, longitude: Double): Boolean {
        val scaledGridSize = PLACE_GRID_METER / (Km_PER_LATLNG_DEGREE)
        val deltaLatScaled = kotlin.math.abs(photo.latitude - latitude) * 1000
        val deltaLngScaled = kotlin.math.abs(photo.longitude - longitude) * 1000
        val deltaScaled = sqrt(deltaLatScaled.pow(2.0) + deltaLngScaled.pow(2.0))
        return (deltaScaled < scaledGridSize)
    }

    /**
     * Set similar time when the difference is within 1 hour.
     */
    fun isSimilarTime(photo: PhotoInitialTraceResponse, time: Date): Boolean {
        val diffTime = kotlin.math.abs(photo.timestamp.time - time.time)
        return diffTime < 1000 * TIME_GRID_SEC // 1 hour
    }

    /**
     * Group photos by place and time.
     */
    // FIXME: Assumes all photos has perfect metadata
    private fun groupPhotosWithLocationAndTimeAndReturnInitialTraceDTOList(photoEntityList: List<Photo>): MutableList<FootprintInitialTraceResponse> {
        val footprintInitialTraceResponseList: MutableList<FootprintInitialTraceResponse> = mutableListOf()

        photoEntityList.forEach {
            var isAdded = false
            val photo = PhotoInitialTraceResponse(
                id = it.id!!,
                imagePath = it.imagePath,
                imageUrl = imageUrlUtil.getImageURLfromImagePath(it.imagePath),
                latitude = it.latitude,
                longitude = it.longitude,
                timestamp = it.timestamp,
            )
            for (initialTraceDTO in footprintInitialTraceResponseList) {
                // Check if place is near enough
                // If photo has similar place and time with group, add photo to the group and update the group's average place and time
                if (isNearEnough(photo, initialTraceDTO.meanLatitude, initialTraceDTO.meanLongitude) && isSimilarTime(photo, initialTraceDTO.meanTime)) {
                    initialTraceDTO.run {
                        // Add photo to the initialTraceDTO
                        photoList.add(photo)
                        // Normalize coordinates and time, and update startTime and endTime
                        val photoCount = photoList.size
                        meanTime = Date(((meanTime.time * (photoCount - 1)) + photo.timestamp.time) / photoCount)
                        meanLatitude = ((meanLatitude * (photoCount - 1)) + photo.latitude) / photoCount
                        meanLongitude = ((meanLongitude * (photoCount - 1)) + photo.longitude) / photoCount
                        startTime = if (startTime.time <= photo.timestamp.time) { startTime } else { photo.timestamp }
                        endTime = if (endTime.time >= photo.timestamp.time) { endTime } else { photo.timestamp }
                    }

                    isAdded = true
                    break
                }
            }
            // If photo didn't add to any groups, create new group.
            if (!isAdded) {
                val newFootprintInitialTraceResponse = FootprintInitialTraceResponse(
                    meanLatitude = photo.latitude,
                    meanLongitude = photo.longitude,
                    photoList = mutableListOf(photo),
                    meanTime = photo.timestamp,
                    startTime = photo.timestamp,
                    endTime = photo.timestamp,
                    recommendedPlaceList = mutableListOf()
                )
                footprintInitialTraceResponseList.add(newFootprintInitialTraceResponse)
            }
        }

        return footprintInitialTraceResponseList.sortedBy { it.startTime }.toMutableList()
    }

    /**
     * Add recomended place list to initialtracedtolist given.
     * Categories are (음식점, 카페, 관광명소, 문화시설, 숙박)
     * Radius is meter.
     */
    private fun addRecomendedPlaceToInitialTraceDTOList(footprintInitialTraceResponseList: MutableList<FootprintInitialTraceResponse>, radius: Int) {
        footprintInitialTraceResponseList.forEach {
            val loop = { radius: Int ->
                for (category in listOf(TAG_CODE.음식점, TAG_CODE.관광명소, TAG_CODE.문화시설, TAG_CODE.카페, TAG_CODE.숙박)) {
                    // Get places for each category and add to recommendedPlaceList
                    val responseEntityPlace = kakaoAPIService.coordToPlace(it.meanLongitude.toString(), it.meanLatitude.toString(), category.code, radius)
                    val objectMapper = ObjectMapper()
                    val body = objectMapper.readValue(responseEntityPlace.body, Map::class.java)
                    val documents = body["documents"] as ArrayList<Map<String, String>>
                    documents.forEach { map ->
                        it.recommendedPlaceList.add(
                            PlaceInitialTraceResponse(
                                name = map["place_name"]!!,
                                address = map["address_name"]!!,
                                distance = map["distance"]!!.toInt(),
                                category = TagResponse(category.ordinal, category.name)
                            )
                        )
                    }
                }
            }
            // Loop to find near places, by increasing radius until finds at least one place (maximum 10 loops)
            for (i in 1..10) {
                loop(radius * i)
                if (it.recommendedPlaceList.isNotEmpty()) { break }
            }

            // Sort by distance (shorter distance => higher priority)
            it.recommendedPlaceList.sortBy { place -> place.distance }
        }
    }
}
