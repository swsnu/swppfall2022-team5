package com.swpp.footprinter.domain.photo.model

data class PhotoInitialTraceDTO(
    val id: Long,
    val imagePath: String,
    val longitude: String,
    val latitude: String,
    val timestamp: String,
)
