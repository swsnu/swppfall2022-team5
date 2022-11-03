package com.swpp.footprinter.domain.tag.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.domain.footprint.model.Footprint
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Tag(

    @Column(name = "tag_name")
    val tagName: String,

    @OneToMany(mappedBy = "tag")
    val taggedFootprints: List<Footprint>,

) : BaseEntity()
