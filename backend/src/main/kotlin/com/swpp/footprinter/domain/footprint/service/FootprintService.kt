package com.swpp.footprinter.domain.footprint.service

import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.common.utils.stringToDate8601
import com.swpp.footprinter.domain.footprint.dto.FootprintRequest
import com.swpp.footprinter.domain.footprint.dto.FootprintResponse
import com.swpp.footprinter.domain.footprint.model.Footprint
import com.swpp.footprinter.domain.footprint.repository.FootprintRepository
import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.photo.repository.PhotoRepository
import com.swpp.footprinter.domain.photo.service.PhotoService
import com.swpp.footprinter.domain.place.model.Place
import com.swpp.footprinter.domain.place.repository.PlaceRepository
import com.swpp.footprinter.domain.tag.model.Tag
import com.swpp.footprinter.domain.tag.repository.TagRepository
import com.swpp.footprinter.domain.trace.model.Trace
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface FootprintService {
    fun getFootprintById(footprintId: Long): FootprintResponse
    fun createFootprintAndReturn(request: FootprintRequest, trace: Trace): Footprint
    fun editFootprint(footprintId: Long, request: FootprintRequest)
    fun deleteFootprintById(footprintId: Long)
}

@Service
class FootprintServiceImpl(
    private val footprintRepo: FootprintRepository,
    private val placeRepo: PlaceRepository,
    private val tagRepo: TagRepository,
    private val photoRepo: PhotoRepository,
    private val photoService: PhotoService,
) : FootprintService {
    override fun getFootprintById(footprintId: Long): FootprintResponse {
        val footprint = footprintRepo.findByIdOrNull(footprintId) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        return footprint.toResponse()
    }

    @Transactional
    override fun createFootprintAndReturn(request: FootprintRequest, trace: Trace): Footprint {
        val footprint = Footprint(
            startTime = stringToDate8601(request.startTime!!),
            endTime = stringToDate8601(request.endTime!!),
            rating = request.rating!!,
            trace = trace,
            memo = request.memo!!,
            place = request.place!!.let { placeRequest -> // If place exists, use that one. Else, create new one
                placeRepo.findByNameAndAddress(
                    placeRequest.name!!, placeRequest.address!!
                )
                    ?: Place(
                        name = placeRequest.name,
                        address = placeRequest.address,
                        footprints = mutableSetOf()
                    )
            },
            tag = request.tag!!.let { t -> // If tag exists, use that one. Else, create new one
                tagRepo.findByTagName(t)
                    ?: Tag(tagName = t, taggedFootprints = mutableSetOf())
            },
            photos = request.photos.map {
                photoRepo.findByImagePath(it.imagePath!!)
                    ?: Photo(
                        imagePath = it.imagePath,
                        longitude = it.longitude!!,
                        latitude = it.latitude!!,
                        timestamp = stringToDate8601(it.timestamp!!),
                        footprint = null,
                    )
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
    override fun editFootprint(footprintId: Long, request: FootprintRequest) {
        val target = footprintRepo.findByIdOrNull(footprintId) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        target.startTime = stringToDate8601(request.startTime!!)
        target.endTime = stringToDate8601(request.endTime!!)
        target.rating = request.rating!!
        target.memo = request.memo!!
        // Remove footprint in original tag, and add new editted tag (if two of those are different.)
        target.tag = target.tag.let {
            if (it.tagName != request.tag!!) {
                it.taggedFootprints.remove(target)
                val editTag = tagRepo.findByTagName(request.tag)
                editTag?.apply { this.taggedFootprints.add(target) }
                    ?: Tag(tagName = request.tag, taggedFootprints = mutableSetOf(target))
            } else {
                it
            }
        }
        // Remove footprint in original place, and add new editted place (if two places are different).
        // Also, remove place when no footprint joined to place
        target.place = request.place!!.let {
            if (!(it.name!! == target.place.name && it.address!! == target.place.address)) {
                // Remove footprint in original place, and clean original place if possible
                target.place.footprints.remove(target)
                if (target.place.footprints.isEmpty()) { placeRepo.delete(target.place) }
                // Change to new place.
                val editPlace = placeRepo.findByNameAndAddress(it.name, it.address!!)
                editPlace?.apply { this.footprints.add(target) }
                    ?: Place(
                        name = it.name,
                        address = it.address,
                        footprints = mutableSetOf(target)
                    )
            } else {
                target.place
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
                val addPhoto = Photo(
                    imagePath = it.imagePath!!,
                    latitude = it.latitude!!,
                    longitude = it.longitude!!,
                    timestamp = stringToDate8601(it.timestamp!!),
                    footprint = target
                )
                photoRepo.save(addPhoto)
                this.add(addPhoto)
            }
        }
    }

    override fun deleteFootprintById(footprintId: Long) {
        footprintRepo.deleteById(footprintId)
    }
}
