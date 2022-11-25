package com.swpp.footprinter.domain.user.api

import com.swpp.footprinter.domain.user.service.UserService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val service: UserService
) {
}
