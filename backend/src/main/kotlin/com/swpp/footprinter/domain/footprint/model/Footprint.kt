package com.swpp.footprinter.domain.footprint.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.domain.memo.model.Memo
import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.place.model.Place
import com.swpp.footprinter.domain.tag.model.Tag
import com.swpp.footprinter.domain.trace.model.Trace
import javax.persistence.*

@Entity
class Footprint(

    @Column(name = "start_time", nullable = false)
    val startTime: String,

    @Column(name = "end_time", nullable = false)
    val endTime: String,

    @Column(name = "rating", nullable = false)
    val rating: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "traceId")
    val trace: Trace,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "placeId")
    val place: Place,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "tagId")
    val tag: Tag,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "memoId")
    val memo: Memo,

    @OneToMany(mappedBy = "footprint")
    val photos: List<Photo> = listOf(),

    ) : BaseEntity()
