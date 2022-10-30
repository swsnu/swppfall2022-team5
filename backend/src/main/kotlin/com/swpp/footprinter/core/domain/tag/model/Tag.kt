package com.swpp.footprinter.core.domain.tag.model

import com.swpp.footprinter.core.common.model.BaseEntity
import com.swpp.footprinter.core.domain.footprint.model.Footprint
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Tag(

    @Column(name = "tag_name")
    val tagName: String,

    @OneToMany(mappedBy = "tag")
    val taggedFootprint: List<Footprint>,

) : BaseEntity()
