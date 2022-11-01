package com.swpp.footprinter.domain.photo.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.domain.footprint.model.Footprint
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
    @JoinColumn(referencedColumnName = "id", name = "footprintId")
    val footprint: Footprint,

    ) : BaseEntity()
