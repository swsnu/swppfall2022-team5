package com.swpp.footprinter.domain.auth.dto

data class SignInRequest(
    val username: String,
    val password: String,
)
