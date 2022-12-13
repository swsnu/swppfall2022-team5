package com.swpp.footprinter.domain.user.dto

import javax.validation.constraints.NotEmpty

data class UserModifyRequest(
    @field: NotEmpty
    val password: String,
    val imagePath: String? = null,
)
