package com.swpp.footprinter.domain.user.api

import com.swpp.footprinter.common.annotations.UserContext
import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.domain.user.dto.UserModifyRequest
import com.swpp.footprinter.domain.user.dto.UserResponse
import com.swpp.footprinter.domain.user.model.User
import com.swpp.footprinter.domain.user.service.UserService
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/user")
class UserController(private val userService: UserService) {
    @GetMapping("/{username}")
    @ResponseBody
    fun getUserResponseByUsername(@PathVariable username: String): UserResponse {
        return userService.getUserByUsername(username)
    }

    @PutMapping("/{username}")
    fun putUser(
        @UserContext loginUser: User,
        @PathVariable username: String,
        @Valid @RequestBody userModifyRequest: UserModifyRequest,
        bindingResult: BindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw FootprinterException(ErrorType.WRONG_FORMAT)
        } else if (username != loginUser.username) {
            throw FootprinterException(ErrorType.UNAUTHORIZED)
        }
        return userService.modifyUser(loginUser, userModifyRequest)
    }
}
