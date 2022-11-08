package com.swpp.footprinter.domain.place.dto

import com.swpp.footprinter.domain.tag.dto.TagResponse

data class PlaceInitialTraceResponse(
    val name: String,
    val address: String,
    val distance: Int,
    val category: TagResponse
)
