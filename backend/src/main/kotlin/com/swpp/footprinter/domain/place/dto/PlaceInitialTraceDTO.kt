package com.swpp.footprinter.domain.place.dto

import com.swpp.footprinter.domain.place.service.externalAPI.CATEGORY_CODE

data class PlaceInitialTraceDTO(
    val name: String,
    val address: String,
    val distance: Int,
    val category: CATEGORY_CODE,
)
