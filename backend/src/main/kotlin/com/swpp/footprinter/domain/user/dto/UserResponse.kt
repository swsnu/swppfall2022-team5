package com.swpp.footprinter.domain.user.dto

data class UserResponse(
    val username: String,
    val followingCount: Int,
    val followerCount: Int,
    val traceCount: Int,
)
