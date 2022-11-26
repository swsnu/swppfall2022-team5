package com.swpp.footprinter.domain.auth.dto

import javax.validation.constraints.NotEmpty

data class TokenVerifyResponse(
    @field: NotEmpty
    val valid: Boolean? = null,
)
