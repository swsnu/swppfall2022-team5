package com.swpp.footprinter.domain.user.dto

import javax.validation.constraints.NotEmpty

data class UserRequest(
    @field: NotEmpty
    val username: String?
)
