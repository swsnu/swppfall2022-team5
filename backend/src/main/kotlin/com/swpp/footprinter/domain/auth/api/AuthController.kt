package com.swpp.footprinter.domain.auth.api

import com.swpp.footprinter.common.annotations.UserContext
import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.common.utils.ImageUrlUtil
import com.swpp.footprinter.domain.auth.dto.*
import com.swpp.footprinter.domain.auth.service.AuthService
import com.swpp.footprinter.domain.auth.service.AuthTokenService
import com.swpp.footprinter.domain.user.model.User
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class AuthController(
    private val authTokenService: AuthTokenService,
    private val authService: AuthService,
    private val imageUrlUtil: ImageUrlUtil,
) {

    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody signUpRequest: SignUpRequest, bindingResult: BindingResult): AuthResponse {
        if (bindingResult.hasErrors()) {
            throw FootprinterException(ErrorType.WRONG_FORMAT)
        }

        val user = try { authService.createUser(signUpRequest) } catch (e: DataIntegrityViolationException) { throw FootprinterException(ErrorType.DUPLICATE_USERNAME) }
        val jwt = authTokenService.generateTokenByUsername(username = user.username)
        return AuthResponse(accessToken = jwt)
    }

    @PostMapping("/signin")
    fun logIn(@Valid @RequestBody signInRequest: SignInRequest, bindingResult: BindingResult): AuthResponse {
        if (bindingResult.hasErrors()) {
            throw FootprinterException(ErrorType.WRONG_FORMAT)
        }
        val user = authService.findUser(username = signInRequest.username!!, password = signInRequest.password!!) ?: throw FootprinterException(ErrorType.INVALID_USER_INFO)

        val jwt = authTokenService.generateTokenByUsername(username = user.username)
        return AuthResponse(accessToken = jwt)
    }

    @GetMapping("/me")
    fun getMe(@UserContext user: User) = user.toResponse(imageUrlUtil)

    @PostMapping("/token")
    fun verifyToken(@Valid @RequestBody token: TokenVerifyRequest): TokenVerifyResponse {
        return if (authTokenService.verifyToken(token.token.orEmpty())) TokenVerifyResponse(true)
        else TokenVerifyResponse(false)
    }
}
