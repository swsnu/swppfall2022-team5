package com.swpp.footprinter.domain.auth.dto

import javax.validation.constraints.NotEmpty

data class SignInRequest(
    @field: NotEmpty
    val username: String? = null,
    @field: NotEmpty
    val password: String? = null,
)
