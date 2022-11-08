package com.swpp.footprinter.domain.tag.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.domain.footprint.model.Footprint
import com.swpp.footprinter.domain.tag.dto.TagResponse
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Tag(

    @Column(name = "tag_name", unique = true)
    val tagName: String,

    @OneToMany(mappedBy = "tag")
    val taggedFootprints: MutableSet<Footprint>,

) : BaseEntity() {
    fun toResponse() = TagResponse(tagName)
}
