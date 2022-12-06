package com.swpp.footprinter.domain.user.api

import com.swpp.footprinter.domain.user.dto.UserResponse
import com.swpp.footprinter.domain.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(private val userService: UserService) {
    @GetMapping("/{username}")
    @ResponseBody
    fun getUserResponseByUsername(@PathVariable username: String): UserResponse {
        return userService.getUserByUsername(username)
    }
}
