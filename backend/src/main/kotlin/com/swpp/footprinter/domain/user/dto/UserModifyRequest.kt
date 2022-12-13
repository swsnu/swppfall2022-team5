package com.swpp.footprinter.domain.user.dto

data class UserModifyRequest(
//    @field: NotEmpty
    val password: String? = null,
    val imagePath: String? = null,
)
