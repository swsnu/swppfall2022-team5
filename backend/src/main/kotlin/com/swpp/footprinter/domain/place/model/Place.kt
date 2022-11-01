package com.swpp.footprinter.domain.place.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.domain.footprint.model.Footprint
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Place(

    @Column(name = "city")
    val city: String,

    @Column(name = "county")
    val county: String,

    @Column(name = "district")
    val district: String,

    @OneToMany(mappedBy = "place")
    val footprints: List<Footprint>,

) : BaseEntity()
