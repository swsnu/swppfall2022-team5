package com.swpp.footprinter.domain.user.api

import com.swpp.footprinter.common.annotations.UserContext
import com.swpp.footprinter.domain.user.dto.UserFollowResponse
import com.swpp.footprinter.domain.user.dto.UserRequest
import com.swpp.footprinter.domain.user.dto.UserResponse
import com.swpp.footprinter.domain.user.model.User
import com.swpp.footprinter.domain.user.service.UserFollowService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/user")
class UserFollowController(
    private val service: UserFollowService
) {
    @GetMapping("/follow/{username}")
    @ResponseBody
    fun getUserFollowCount(
        @UserContext loginUser: User,
        @PathVariable username: String
    ): UserFollowResponse {
        return service.getUserFollowCount(loginUser, username)
    }

    @GetMapping("/followers/{username}")
    @ResponseBody
    fun getUserFollowers(
        @UserContext loginUser: User,
        @PathVariable username: String
    ): List<UserResponse> {
        return service.getUserFollowers(loginUser, username)
    }

    @GetMapping("/followings/{username}")
    @ResponseBody
    fun getUserFollowings(
        @UserContext loginUser: User,
        @PathVariable username: String
    ): List<UserResponse> {
        return service.getUserFollowings(loginUser, username)
    }

    @PostMapping("/followings")
    fun followUser(
        @RequestBody @Valid followedUser: UserRequest,
        @UserContext loginUser: User
    ) {
        try {
            service.followUser(loginUser, followedUser.username!!)
        } catch (e: DataIntegrityViolationException) {
            return
        }
    }

    @DeleteMapping("/followings")
    fun unfollowUser(
        @RequestBody @Valid followedUser: UserRequest,
        @UserContext loginUser: User
    ) {
        service.unfollowUser(loginUser, followedUser.username!!)
    }

    @DeleteMapping("/followers")
    fun deleteFollower(
        @RequestBody @Valid follower: UserRequest,
        @UserContext loginUser: User
    ) {
        service.deleteFollower(loginUser, follower.username!!)
    }
}
