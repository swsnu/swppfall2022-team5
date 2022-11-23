package com.swpp.footprinter.domain.place.dto

data class PlaceResponse(
    val name: String,
    val address: String,
    val distance: Double ? = null,
    val tagId: Int? = null,
)
