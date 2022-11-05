package com.swpp.footprinter.common.exception

data class ErrorResponse(
    val error: ErrorInfo
)

data class ErrorInfo(
    val code: Int,
    val message: String,
)
