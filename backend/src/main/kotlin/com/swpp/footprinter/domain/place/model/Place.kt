package com.swpp.footprinter.domain.place.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.domain.footprint.model.Footprint
import com.swpp.footprinter.domain.place.dto.PlaceResponse
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Place(
    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val address: String,

//    @ManyToOne() //TODO Maybe...?
//    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
//    val tag: Tag,

    @OneToMany(mappedBy = "place")
    val footprints: MutableSet<Footprint>,

) : BaseEntity() {
    fun toResponse() = PlaceResponse(name, address)
}
