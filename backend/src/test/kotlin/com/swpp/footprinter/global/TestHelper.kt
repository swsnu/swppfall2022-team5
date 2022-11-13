package com.swpp.footprinter.global

import com.swpp.footprinter.domain.footprint.model.Footprint
import com.swpp.footprinter.domain.footprint.repository.FootprintRepository
import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.photo.repository.PhotoRepository
import com.swpp.footprinter.domain.place.model.Place
import com.swpp.footprinter.domain.place.repository.PlaceRepository
import com.swpp.footprinter.domain.tag.TAG_CODE
import com.swpp.footprinter.domain.tag.model.Tag
import com.swpp.footprinter.domain.tag.repository.TagRepository
import com.swpp.footprinter.domain.trace.model.Trace
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.model.User
import com.swpp.footprinter.domain.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class TestHelper @Autowired constructor(
    private val footprintRepo: FootprintRepository,
    private val placeRepo: PlaceRepository,
    private val tagRepo: TagRepository,
    private val userRepo: UserRepository,
    private val traceRepo: TraceRepository,
    private val photoRepo: PhotoRepository,
) {

    fun createUser(
        username: String,
        email: String,
        myTrace: MutableSet<Trace> = mutableSetOf()
    ) = User(username, email).also { userRepo.save(it) }

    fun createPlace(
        name: String,
        address: String,
        footprints: MutableSet<Footprint> = mutableSetOf()
    ) = Place(name, address, footprints).also { placeRepo.save(it) }

    fun createTag(
        tagCode: TAG_CODE,
        taggedFootprints: MutableSet<Footprint> = mutableSetOf()
    ) = Tag(tagCode, taggedFootprints).also { tagRepo.save(it) }

    fun createPhoto(
        imagePath: String,
        longitude: Double,
        latitude: Double,
        timestamp: Date,
        footprint: Footprint? = null
    ) = Photo(imagePath, longitude, latitude, timestamp, footprint).also { photoRepo.save(it) }

    fun createFootprint(
        startTime: Date,
        endTime: Date,
        rating: Int,
        trace: Trace,
        place: Place,
        tag: Tag,
        memo: String,
        photos: MutableSet<Photo> = mutableSetOf()
    ) = Footprint(startTime, endTime, rating, trace, place, tag, memo, photos)
        .also { footprintRepo.save(it) }

    fun createTrace(
        traceTitle: String,
        traceDate: String,
        owner: User,
        footprints: MutableSet<Footprint> = mutableSetOf(),
    ) = Trace(traceTitle, traceDate, owner, footprints).also { traceRepo.save(it) }

    fun initializeTag() {
        for (tagCode in TAG_CODE.values()) {
            createTag(tagCode)
        }
    }

    fun createFootprintAndUpdateElements(
        startTime: Date,
        endTime: Date,
        rating: Int,
        trace: Trace,
        place: Place,
        tag: Tag,
        memo: String,
        photos: MutableSet<Photo> = mutableSetOf()
    ) = createFootprint(startTime, endTime, rating, trace, place, tag, memo, photos)
        .apply {
            place.footprints.add(this)
            placeRepo.save(place)
            tag.taggedFootprints.add(this)
            tagRepo.save(tag)
            photos.forEach { it.footprint = this }
            photoRepo.saveAll(photos)
            trace.footprints.add(this)
            traceRepo.save(trace)
        }
}
