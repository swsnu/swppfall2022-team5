package com.swpp.footprinter.domain.footprint.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.common.utils.dateToString8601
import com.swpp.footprinter.domain.footprint.dto.FootprintResponse
import com.swpp.footprinter.domain.memo.model.Memo
import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.place.model.Place
import com.swpp.footprinter.domain.tag.model.Tag
import com.swpp.footprinter.domain.trace.model.Trace
import java.util.*
import javax.persistence.*

@Entity
class Footprint(
    var startTime: Date,
    var endTime: Date,

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
            startTime = dateToString8601(startTime),
            endTime = dateToString8601(endTime),
            rating = rating,
            trace = trace,
            place = place,
            tag = tag,
            memo = memo,
            photos = photos
        )
    }
}
