package com.swpp.footprinter.domain.user.api

import com.swpp.footprinter.common.annotations.UserContext
import com.swpp.footprinter.domain.user.dto.UserRequest
import com.swpp.footprinter.domain.user.dto.UserResponse
import com.swpp.footprinter.domain.user.model.User
import com.swpp.footprinter.domain.user.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val service: UserService
) {
    @GetMapping("/{username}/followers")
    @ResponseBody
    fun getUserFollowers(
        @UserContext loginUser: User,
        @PathVariable username: String
    ): List<UserResponse> {
        return service.getUserFollowers(loginUser, username)
    }

    @GetMapping("/{username}/followings")
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
        service.followUser(loginUser, followedUser.username!!)
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
