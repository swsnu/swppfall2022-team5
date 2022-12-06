package com.swpp.footprinter.domain.trace.dto

import com.swpp.footprinter.domain.user.dto.UserResponse

data class TraceResponse(
    val id: Long,
    val date: String?,
    val title: String?,
    val owner: UserResponse,
    val likesCount: Int,
)
