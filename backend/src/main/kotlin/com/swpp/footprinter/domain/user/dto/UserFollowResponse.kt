package com.swpp.footprinter.domain.user.dto

data class UserFollowResponse(
    val followerCount: Int,
    val followingCount: Int,
)

data class UserFollowingResponse(
    val isFollowing: Boolean,
    val isFollowed: Boolean
)
