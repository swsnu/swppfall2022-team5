package com.swpp.footprinter.domain.trace.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.domain.footprint.model.Footprint
import com.swpp.footprinter.domain.footprint.repository.FootprintRepository
import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.photo.dto.PhotoInitialTraceDTO
import com.swpp.footprinter.domain.photo.repository.PhotoRepository
import com.swpp.footprinter.domain.place.dto.PlaceInitialTraceDTO
import com.swpp.footprinter.domain.place.service.externalAPI.CATEGORY_CODE
import com.swpp.footprinter.domain.place.service.externalAPI.KakaoAPIService
import com.swpp.footprinter.domain.trace.dto.InitialTraceDTO
import com.swpp.footprinter.domain.trace.dto.TraceRequest
import com.swpp.footprinter.domain.trace.dto.TraceResponse
import com.swpp.footprinter.domain.trace.model.Trace
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.repository.UserRepository
import com.swpp.footprinter.utils.stringToDate8601
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.Math.*
import java.util.Date
import kotlin.math.pow
import kotlin.math.sqrt

interface TraceService {
    fun getAllTraces(): List<TraceResponse>
    fun createTrace(request: TraceRequest)
    fun getTraceById(traceId: Long): TraceResponse
    fun deleteTraceById(traceId: Long)

    fun createInitialTraceBasedOnPhotoIdListGiven(photoIds: List<Long>): List<InitialTraceDTO> // List<Pair<Place, List<Photo>>>
}

@Service
class TraceServiceImpl(
    private val traceRepo: TraceRepository,
    private val footprintRepo: FootprintRepository,
    private val userRepo: UserRepository,
    private val photoRepo: PhotoRepository,
    private val kakaoAPIService: KakaoAPIService,
) : TraceService {
    override fun getAllTraces(): List<TraceResponse> {
        return traceRepo.findAll().filter { it.owner != userRepo.findByIdOrNull(1)!! }.map { trace -> trace.toResponse() } // TODO: 현재 user로 넣기
    }

    override fun createTrace(request: TraceRequest) {
        val newTrace = Trace(
            traceTitle = request.title!!,
            traceDate = request.date!!,
            owner = userRepo.findByIdOrNull(3)!!, // TODO: 현재 user로 넣기
            footprintList = listOf()
        )
        val newId = traceRepo.save(newTrace).id
        for (footprint in request.footprintList!!) {
            footprintRepo.save(
                Footprint(
                    startTime = footprint.startTime!!,
                    endTime = footprint.endTime!!,
                    rating = footprint.rating!!,
                    trace = newTrace,
                    place = footprint.place!!,
                    tag = footprint.tag!!,
                    memo = footprint.memo!!,
                    photos = footprint.photos!!
                )
            )
        }
    }

    override fun getTraceById(traceId: Long): TraceResponse {
        val trace = traceRepo.findByIdOrNull(traceId) ?: TODO("존재하지 않는 trace")
        return trace.toResponse()
    }

    override fun deleteTraceById(traceId: Long) {
        traceRepo.deleteById(traceId) // TODO: Authentication
    }

    override fun createInitialTraceBasedOnPhotoIdListGiven(photoIds: List<Long>): List<InitialTraceDTO> {
        val photoEntityList: List<Photo> = photoIds.map {
            photoRepo.findByIdOrNull(it) ?: throw FootprinterException(ErrorType.NOT_FOUND) }

        val initialTraceDTOList = groupPhotosWithLocationAndTimeAndReturnInitialTraceDTOList(photoEntityList)

        addRecomendedPlaceToInitialTraceDTOList(initialTraceDTOList, radius=60)

        return initialTraceDTOList
    }


    /**
    * Assume GPS error is 30m, and 1 degree of lat/lng is 1112km.
    * => Assume there is same place within 0.000027 degree of lat/lng.
    */
    fun isNearEnough(photo: PhotoInitialTraceDTO, latitude: Double, longitude: Double): Boolean {
        val deltaLatScaled = abs(photo.latitude - latitude) * 10000
        val deltaLngScaled = abs(photo.longitude - longitude) * 10000
        val deltaScaled = sqrt(deltaLatScaled.pow(2.0) + deltaLngScaled.pow(2.0))
        return (deltaScaled < 2.7)
    }

    /**
    * Set similar time when the difference is within 1 hour.
    */
    fun isSimilarTime(photo: PhotoInitialTraceDTO, time: Date): Boolean {
        val diffTime = abs(photo.timestamp.time - time.time)
        return diffTime < 3600000 // 1 hour
    }

    /**
     * Group photos by place and time.
     */
    // FIXME: Assumes all photos has perfect metadata
    private fun groupPhotosWithLocationAndTimeAndReturnInitialTraceDTOList(photoEntityList: List<Photo>): MutableList<InitialTraceDTO>  {
        val initialTraceDTOList: MutableList<InitialTraceDTO> = mutableListOf()

        val isSimilarTime: (PhotoInitialTraceDTO, Date) -> Boolean = { photo, time ->
            val diffTime = abs(photo.timestamp.time - time.time)
            diffTime < 3600000 // 1 hour
        }

        photoEntityList.forEach{
            var isAdded = false
            val photo = PhotoInitialTraceDTO(
                id=it.id!!, imagePath=it.imagePath, latitude=it.latitude.toDouble(), longitude=it.longitude.toDouble(), timestamp= stringToDate8601(it.timestamp)
            )
            for (initialTraceDTO in initialTraceDTOList) {
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
                val newInitialTraceDTO = InitialTraceDTO(
                    meanLatitude = photo.latitude,
                    meanLongitude = photo.longitude,
                    photoList = mutableListOf(photo),
                    meanTime = photo.timestamp,
                    startTime = photo.timestamp,
                    endTime = photo.timestamp,
                    recommendedPlaceList = mutableListOf()
                )
                initialTraceDTOList.add(newInitialTraceDTO)
            }
        }

        return initialTraceDTOList
    }

    /**
     * Add recomended place list to initialtracedtolist given.
     * Categories are (음식점, 카페, 관광명소, 문화시설, 숙박)
     * Radius is meter.
     */
    private fun addRecomendedPlaceToInitialTraceDTOList(initialTraceDTOList: MutableList<InitialTraceDTO>, radius: Int) {
        initialTraceDTOList.forEach{
            for (category in listOf(CATEGORY_CODE.음식점, CATEGORY_CODE.관광명소, CATEGORY_CODE.문화시설, CATEGORY_CODE.카페, CATEGORY_CODE.숙박)) {
                // Get places for each category and add to recommendedPlaceList
                val responseEntityPlace = kakaoAPIService.coordToPlace(it.meanLongitude.toString(), it.meanLatitude.toString(), category.code, radius)
                val objectMapper = ObjectMapper()
                val body = objectMapper.readValue(responseEntityPlace.body, Map::class.java)
                val documents = body["documents"] as ArrayList<Map<String, String>>
                documents.forEach { map ->
                    it.recommendedPlaceList.add(PlaceInitialTraceDTO(
                        name = map["place_name"]!!,
                        address = map["address_name"]!!,
                        distance = map["distance"]!!.toInt(),
                        category,
                    ))
                }
            }
            // Sort by distance (shorter distance => higher priority)
            it.recommendedPlaceList.sortBy{ place -> place.distance }
        }
    }
}
