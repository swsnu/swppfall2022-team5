package com.swpp.footprinter.domain.place.dto

import com.swpp.footprinter.common.TAG_CODE

data class PlaceInitialTraceResponse(
    val name: String,
    val address: String,
    val distance: Int,
    val category: TAG_CODE,
)
