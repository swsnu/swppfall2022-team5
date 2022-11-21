package com.swpp.footprinter.domain.auth.service

import com.swpp.footprinter.domain.auth.dto.SignUpRequest
import com.swpp.footprinter.domain.user.model.User
import com.swpp.footprinter.domain.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface AuthService {
    fun createUser(signUpRequest: SignUpRequest): User

    fun findUser(username: String, password: String): User?
}

@Service
class AuthServiceImpl(
    private val userRepo: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : AuthService {
    override fun createUser(signUpRequest: SignUpRequest): User {
        return userRepo.save(User(username = signUpRequest.username!!, password = passwordEncoder.encode(signUpRequest.password)))
    }

    override fun findUser(username: String, password: String): User? {
        val user = userRepo.findByUsername(username) ?: return null
        if (passwordEncoder.matches(password, user.password)) {
            return user
        }
        return null
    }
}
