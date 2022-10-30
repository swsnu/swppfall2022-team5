package com.swpp.footprinter.core.domain.tag.model

import com.swpp.footprinter.core.common.model.BaseEntity
import com.swpp.footprinter.core.domain.footprint.model.Footprint
import javax.persistence.Column
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

class Tag(

    @Column(name = "tag_name")
    val tagName: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "footprint_id")
    val taggedFootprint: List<Footprint>,

) : BaseEntity()
