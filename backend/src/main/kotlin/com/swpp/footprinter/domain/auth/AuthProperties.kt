package com.swpp.footprinter.domain.auth

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("auth.jwt")
data class AuthProperties(
    val issuer: String,
    val jwtSecret: String,
    val jwtExpiration: Long,
)
