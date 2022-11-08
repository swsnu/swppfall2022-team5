package com.swpp.footprinter.domain.tag.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.domain.footprint.model.Footprint
import com.swpp.footprinter.domain.tag.TAG_CODE
import com.swpp.footprinter.domain.tag.dto.TagResponse
import javax.persistence.*

@Entity
class Tag(

    @Enumerated(value = EnumType.ORDINAL)
    val tagCode: TAG_CODE,

    @OneToMany(mappedBy = "tag")
    val taggedFootprints: MutableSet<Footprint>,

) : BaseEntity() {
    fun toResponse() = TagResponse(tagCode.ordinal, tagCode.name)
}
