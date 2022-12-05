package com.swpp.footprinter.domain.trace.dto

import com.swpp.footprinter.domain.footprint.dto.FootprintResponse
import com.swpp.footprinter.domain.user.dto.UserResponse

class TraceDetailResponse(
    val id: Long,
    val date: String?,
    val title: String?,
    val owner: UserResponse,
    var footprints: List<FootprintResponse>?
)
