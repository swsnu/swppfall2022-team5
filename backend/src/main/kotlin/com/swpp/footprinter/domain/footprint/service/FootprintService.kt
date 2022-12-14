package com.swpp.footprinter.domain.footprint.service

import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.common.utils.ImageUrlUtil
import com.swpp.footprinter.common.utils.stringToDate8601
import com.swpp.footprinter.domain.footprint.dto.FootprintRequest
import com.swpp.footprinter.domain.footprint.dto.FootprintResponse
import com.swpp.footprinter.domain.footprint.model.Footprint
import com.swpp.footprinter.domain.footprint.repository.FootprintRepository
import com.swpp.footprinter.domain.photo.repository.PhotoRepository
import com.swpp.footprinter.domain.photo.service.PhotoService
import com.swpp.footprinter.domain.place.model.Place
import com.swpp.footprinter.domain.place.repository.PlaceRepository
import com.swpp.footprinter.domain.tag.TAG_CODE
import com.swpp.footprinter.domain.tag.repository.TagRepository
import com.swpp.footprinter.domain.trace.model.Trace
import com.swpp.footprinter.domain.user.model.User
import com.swpp.footprinter.domain.trace.repository.TraceLikeRepository
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.IndexOutOfBoundsException
import java.lang.NullPointerException
import javax.transaction.Transactional

interface FootprintService {
    fun getFootprintById(footprintId: Long): FootprintResponse
    fun createFootprintAndReturn(request: FootprintRequest, trace: Trace): Footprint
    fun editFootprint(loginUser: User, footprintId: Long, request: FootprintRequest)
    fun deleteFootprintById(loginUser: User, footprintId: Long)
}

@Service
class FootprintServiceImpl(
    private val footprintRepo: FootprintRepository,
    private val placeRepo: PlaceRepository,
    private val tagRepo: TagRepository,
    private val photoRepo: PhotoRepository,
    private val photoService: PhotoService,
    private val traceRepo: TraceRepository,
    private val traceLikeRepo: TraceLikeRepository,
    private val imageUrlUtil: ImageUrlUtil,
) : FootprintService {
    override fun getFootprintById(footprintId: Long): FootprintResponse {
        val footprint = footprintRepo.findByIdOrNullImproved(footprintId) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        return footprint.toResponse(imageUrlUtil)
    }

    @Transactional
    override fun createFootprintAndReturn(request: FootprintRequest, trace: Trace): Footprint {
        val footprint = Footprint(
            startTime = stringToDate8601(request.startTime!!),
            endTime = stringToDate8601(request.endTime!!),
            rating = request.rating!!,
            trace = trace,
            memo = request.memo!!,
            place = request.place?.let { placeRequest -> // If place exists, use that one. Else, create new one
                placeRepo.findByNameAndAddress(
                    placeRequest.name!!, placeRequest.address!!
                )
                    ?: Place(
                        name = placeRequest.name,
                        address = placeRequest.address,
                        footprints = mutableSetOf()
                    )
            } ?: throw FootprinterException(ErrorType.WRONG_FORMAT),
            tag = request.tagId!!.let { tagId -> // Find the tag and match to it.
                try {
                    tagRepo.findByTagCode(TAG_CODE.values()[tagId])!!
                } catch (e: Exception) {
                    when (e) {
                        is NullPointerException, is IndexOutOfBoundsException ->
                            throw FootprinterException(ErrorType.WRONG_FORMAT)
                        else -> throw e
                    }
                }
            },
            photos = request.photos.map {
                photoRepo.findByImagePath(it.imagePath!!)
                    ?: throw FootprinterException(ErrorType.NOT_FOUND)
            }.toMutableSet(),
        )
        footprintRepo.save(footprint)

        // Update joined tables
        footprint.place.footprints.add(footprint)
        placeRepo.save(footprint.place)

        footprint.tag.taggedFootprints.add(footprint)
        tagRepo.save(footprint.tag)

        footprint.photos.forEach {
            it.footprint = footprint
            photoRepo.save(it)
        }

        return footprint
    }

    @Transactional
    override fun editFootprint(loginUser: User, footprintId: Long, request: FootprintRequest) {
        val target = footprintRepo.findByIdOrNull(footprintId) ?: throw FootprinterException(ErrorType.NOT_FOUND)

        if (target.trace.owner != loginUser) {
            throw FootprinterException(ErrorType.FORBIDDEN)
        }

        target.startTime = stringToDate8601(request.startTime!!)
        target.endTime = stringToDate8601(request.endTime!!)
        target.rating = request.rating!!
        target.memo = request.memo!!
        // Remove footprint in original tag, and add new edited tag (if two of those are different.)
        target.tag = target.tag.let {
            if (it.tagCode.ordinal != request.tagId!!) {
                try {
                    it.taggedFootprints.remove(target)
                    val editTag = tagRepo.findByTagCode(TAG_CODE.values()[request.tagId])
                    editTag!!.apply { this.taggedFootprints.add(target) }
                } catch (e: Exception) {
                    when (e) {
                        is IndexOutOfBoundsException, is NullPointerException ->
                            throw FootprinterException(ErrorType.WRONG_FORMAT)
                        else -> throw e
                    }
                }
            } else {
                it
            }
        }
        // Remove footprint in original place, and add new edited place (if two places are different).
        // Also, remove place when no footprint joined to place
        request.place!!.let {
            if (!(it.name!! == target.place.name && it.address!! == target.place.address)) {
                // Save original place for later use.
                val originalPlace = target.place
                // Change to new place.
                val editPlace = placeRepo.findByNameAndAddress(it.name, it.address!!)
                target.place = editPlace?.apply { this.footprints.add(target) }
                    ?: placeRepo.save(
                        Place(
                            name = it.name,
                            address = it.address,
                            footprints = mutableSetOf(target)
                        )
                    )
                // Remove footprint in original place, and clean original place if possible
                originalPlace.footprints.remove(target)
                if (originalPlace.footprints.isEmpty()) { placeRepo.delete(originalPlace) }
            }
        }
        // Edit target.photo
        // filter out photos to delete, and photos to add
        // assume that all photos are already uploaded
        target.photos.run {
            val requestPhotosPaths = request.photos.map { it.imagePath }
            val targetPhotosPaths = this.map { it.imagePath }
            val toDeletePhotoEntities = this.filter { !requestPhotosPaths.contains(it.imagePath) }
            val toAddPhotoRequests = request.photos.filter { !targetPhotosPaths.contains(it.imagePath) }

            toDeletePhotoEntities.forEach {
                this.remove(it)
                photoService.deletePhotoFromDatabaseAndServer(it)
            }
            toAddPhotoRequests.forEach {
                val addPhoto = photoRepo.findByImagePath(it.imagePath!!) ?: throw FootprinterException(ErrorType.NOT_FOUND)
                addPhoto.footprint = target
                this.add(addPhoto)
            }
        }
    }

    @Transactional
    override fun deleteFootprintById(loginUser: User, footprintId: Long) {
        val target = footprintRepo.findByIdOrNull(footprintId) ?: throw FootprinterException(ErrorType.NOT_FOUND)

        if (target.trace.owner != loginUser) {
            throw FootprinterException(ErrorType.FORBIDDEN)
        }

        val footprint = footprintRepo.findByIdOrNull(footprintId) ?: return
        val place = footprint.place
        val trace = footprint.trace

        // Update trace, place
        trace.footprints.remove(footprint)
        place.footprints.remove(footprint)

        // Delete footprint
        footprintRepo.delete(footprint)

        // Clean trace
        if (trace.footprints.isEmpty()) {
            // Delete All TraceLike connected to trace
            traceLikeRepo.deleteAllByTrace(trace)
            // Delete trace
            traceRepo.delete(trace)
        }

        // Clean place
        if (place.footprints.isEmpty()) {
            placeRepo.delete(place)
        }
    }
}
