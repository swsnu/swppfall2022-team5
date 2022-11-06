package com.swpp.footprinter.domain.photo.model

import java.util.*

data class PhotoInitialTraceDTO(
    val id: Long,
    val imagePath: String,
    val longitude: Double,
    val latitude: Double,
    val timestamp: Date,
)
