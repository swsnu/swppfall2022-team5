package com.swpp.footprinter.domain.photo.dto

import java.util.*

data class PhotoInitialTraceResponse(
    val id: Long,
    val imageUrl: String,
    val longitude: Double,
    val latitude: Double,
    val timestamp: Date,
)
