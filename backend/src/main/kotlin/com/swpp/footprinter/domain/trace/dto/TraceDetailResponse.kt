package com.swpp.footprinter.domain.trace.dto

import com.swpp.footprinter.domain.footprint.dto.FootprintResponse

class TraceDetailResponse(
    val id: Long,
    val date: String?,
    val title: String?,
    val ownerName: String?,
    var footprints: List<FootprintResponse>?,
    val isLiked: Boolean,
    val likesCount: Int
)
