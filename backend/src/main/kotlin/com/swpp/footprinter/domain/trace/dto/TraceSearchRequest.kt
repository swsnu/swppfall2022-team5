package com.swpp.footprinter.domain.trace.dto

import com.swpp.footprinter.domain.place.dto.PlaceRequest
import com.swpp.footprinter.domain.user.dto.UserRequest

data class TraceSearchRequest(
    val users: List<UserRequest>,
    val tags: List<Int>,
    val dates: List<String>,
    val places: List<PlaceRequest>,
    val title: String? = null,
)
