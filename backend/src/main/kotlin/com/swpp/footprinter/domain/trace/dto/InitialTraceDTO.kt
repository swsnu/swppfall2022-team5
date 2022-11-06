package com.swpp.footprinter.domain.trace.dto

import com.swpp.footprinter.domain.photo.dto.PhotoInitialTraceDTO
import com.swpp.footprinter.domain.place.dto.PlaceInitialTraceDTO
import java.util.*

data class InitialTraceDTO(
    val recommendedPlaceList: MutableList<PlaceInitialTraceDTO>,
    val photoList: MutableList<PhotoInitialTraceDTO>,
    var meanLatitude: Double,
    var meanLongitude: Double,
    var meanTime: Date,
    var startTime: Date,
    var endTime: Date,
)
