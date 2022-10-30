package com.swpp.footprinter.core.domain.footprint.model

import com.swpp.footprinter.core.common.model.BaseEntity
import com.swpp.footprinter.core.domain.memo.model.Memo
import com.swpp.footprinter.core.domain.photo.model.Photo
import com.swpp.footprinter.core.domain.place.model.Place
import com.swpp.footprinter.core.domain.tag.model.Tag
import com.swpp.footprinter.core.domain.trace.model.Trace
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
    @JoinColumn(name = "trace_id")
    val trace: Trace,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place")
    val place: Place,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo")
    val memo: Memo,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag")
    val tag: Tag,

    @OneToMany(mappedBy = "footprint")
    val photos: List<Photo> = listOf(),

) : BaseEntity()
