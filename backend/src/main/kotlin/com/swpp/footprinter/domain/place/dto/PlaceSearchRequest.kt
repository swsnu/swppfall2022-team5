package com.swpp.footprinter.domain.place.dto

import javax.validation.constraints.NotNull

data class PlaceSearchRequest(
    val keyword: String,
    val tagIDs: List<Int>,
    @field: NotNull
    val longitude: Double? = null,
    @field: NotNull
    val latitude: Double? = null,
)
