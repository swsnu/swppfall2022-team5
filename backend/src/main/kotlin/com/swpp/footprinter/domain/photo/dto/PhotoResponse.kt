package com.swpp.footprinter.domain.photo.dto

data class PhotoResponse(
    val imagePath: String,
    var imageUrl: String,
    val longitude: Double,
    val latitude: Double,
    val timestamp: String,
    val footprintId: Long?,
)
