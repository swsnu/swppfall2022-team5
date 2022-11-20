package com.swpp.footprinter.domain.auth.service

import com.swpp.footprinter.domain.auth.AuthProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.util.*

@Service
@EnableConfigurationProperties(AuthProperties::class)
class AuthTokenService(
    private val authProperties: AuthProperties,
) {
    private val tokenPrefix = "Bearer "
    private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())

    fun generateTokenByUsername(username: String): String {
        val claims: MutableMap<String, Any> = Jwts.claims()
        val expiryDate = Date(Date().time + authProperties.jwtExpiration)
        claims["username"] = username
        return Jwts.builder()
            .setClaims(claims)
            .setIssuer(authProperties.issuer)
            .setExpiration(expiryDate)
            .signWith(signingKey)
            .compact()
    }

    fun verifyToken(accessToken: String): Boolean {
        return try {
            val jws: Jws<Claims> = parse(accessToken) // Check expiration automatically in here.
            val expiryDate: Date = jws.body.expiration
            expiryDate.after(Date())
        } catch (e: JwtException) {
            false
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun getCurrentUsername(accessToken: String): String? {
        val jws: Jws<Claims> = parse(accessToken)
        return jws.body["username"] as String?
    }

    private fun parse(accessToken: String): Jws<Claims> {
        val prefixRemoved = accessToken.replace(tokenPrefix, "").trim { it <= ' ' }
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(prefixRemoved)
    }

    fun getAccessToken(authHeader: String?): String? {
        if (authHeader?.startsWith(tokenPrefix) == true) {
            return authHeader.replace(tokenPrefix, "").trim()
        }
        return null
    }
}
