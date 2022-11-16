package com.swpp.footprinter.domain.trace.dto

data class TraceResponse(
    val id: Long,
    val date: String?,
    val title: String?,
    val ownerId: Long?,
)
