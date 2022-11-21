package com.swpp.footprinter.common.annotations

import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.domain.auth.service.AuthTokenService
import com.swpp.footprinter.domain.user.model.User
import com.swpp.footprinter.domain.user.repository.UserRepository
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class UserContext

@Component
class UserContextResolver(
    private val authTokenService: AuthTokenService,
    private val userRepository: UserRepository
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(UserContext::class.java)
    }

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): User {
        val token = authTokenService.getAccessToken(webRequest.getHeader("Authorization")) ?: throw FootprinterException(ErrorType.UNAUTHORIZED)
        val username = authTokenService.getCurrentUsername(token) ?: throw FootprinterException(ErrorType.UNAUTHORIZED)
        return userRepository.findByUsername(username) ?: throw FootprinterException(ErrorType.UNAUTHORIZED)
    }
}
