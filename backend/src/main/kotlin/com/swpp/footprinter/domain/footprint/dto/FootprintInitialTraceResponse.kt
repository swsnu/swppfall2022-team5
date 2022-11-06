package com.swpp.footprinter.domain.footprint.dto

import com.swpp.footprinter.domain.photo.dto.PhotoInitialTraceResponse
import com.swpp.footprinter.domain.place.dto.PlaceInitialTraceResponse
import java.util.*

data class FootprintInitialTraceResponse(
    val recommendedPlaceList: MutableList<PlaceInitialTraceResponse>,
    val photoList: MutableList<PhotoInitialTraceResponse>,
    var meanLatitude: Double,
    var meanLongitude: Double,
    var meanTime: Date,
    var startTime: Date,
    var endTime: Date,
)
