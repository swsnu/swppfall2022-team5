package com.swpp.footprinter.domain.photo.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.common.utils.dateToString8601
import com.swpp.footprinter.domain.footprint.model.Footprint
import com.swpp.footprinter.domain.photo.dto.PhotoResponse
import java.util.*
import javax.persistence.*

@Entity
class Photo(

    // Assume that image path is unique
    @Column(name = "image_path", unique = true)
    val imagePath: String,

    @Column(name = "longitude")
    val longitude: Double,

    @Column(name = "latitude")
    val latitude: Double,

    @Column(name = "timestamp")
    @Temporal(value = TemporalType.TIMESTAMP)
    val timestamp: Date,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "footprintId")
    var footprint: Footprint?,

) : BaseEntity() {
    fun toResponse() = PhotoResponse(
        imagePath = imagePath,
        longitude = longitude,
        latitude = latitude,
        timestamp = dateToString8601(timestamp),
        footprintId = footprint?.id
    )
}
