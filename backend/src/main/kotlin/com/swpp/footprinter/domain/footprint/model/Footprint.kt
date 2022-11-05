package com.swpp.footprinter.domain.footprint.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.domain.footprint.dto.FootprintResponse
import com.swpp.footprinter.domain.memo.model.Memo
import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.place.model.Place
import com.swpp.footprinter.domain.tag.model.Tag
import com.swpp.footprinter.domain.trace.model.Trace
import javax.persistence.*

@Entity
class Footprint(

    @Column(name = "start_time", nullable = false)
    var startTime: String,

    @Column(name = "end_time", nullable = false)
    var endTime: String,

    @Column(name = "rating", nullable = false)
    var rating: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "traceId")
    val trace: Trace,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "placeId")
    var place: Place,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "tagId")
    var tag: Tag,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "memoId")
    var memo: Memo,

    @OneToMany(mappedBy = "footprint")
    var photos: List<Photo> = listOf(),

) : BaseEntity() {

    fun toResponse(): FootprintResponse {
        return FootprintResponse(
            id = id!!,
            startTime = startTime,
            endTime = endTime,
            rating = rating,
            trace = trace,
            place = place,
            tag = tag,
            memo = memo,
            photos = photos
        )
    }
}
