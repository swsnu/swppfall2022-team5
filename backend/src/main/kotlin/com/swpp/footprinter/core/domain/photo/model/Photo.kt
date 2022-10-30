package com.swpp.footprinter.core.domain.photo.model

import com.swpp.footprinter.core.common.model.BaseEntity
import com.swpp.footprinter.core.domain.footprint.model.Footprint
import javax.persistence.*

@Entity
class Photo(

    @Column(name = "image_url")
    val imageUrl: String,

    @Column(name = "coordinates")
    val coordinates: String,

    @Column(name = "longitude")
    val longitude: String,

    @Column(name = "latitude")
    val latitude: String,

    @Column(name = "timestamp")
    val timestamp: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "footprint_id")
    val footprint: Footprint,

) : BaseEntity()
