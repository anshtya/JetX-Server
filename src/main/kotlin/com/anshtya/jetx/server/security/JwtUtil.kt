package com.anshtya.jetx.server.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil(
    @param:Value($$"${jwt.secret}")
    private val jwtSecret: String,

    @param:Value($$"${jwt.access-token-expiration}")
    private val accessTokenExpiration: Long,

    @param:Value($$"${jwt.refresh-token-expiration}")
    private val refreshTokenExpiration: Long
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

    fun getToken(request: HttpServletRequest): String {
        return request.getHeader("Authorization").substringAfter("Bearer ")
    }

    fun generateAccessToken(phoneNumber: String, userId: UUID): String {
        return createToken(phoneNumber, userId, accessTokenExpiration)
    }

    fun generateRefreshToken(phoneNumber: String, userId: UUID): String {
        return createToken(phoneNumber, userId, refreshTokenExpiration)
    }

    private fun createToken(
        phoneNumber: String,
        userId: UUID,
        expiration: Long
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)

        return Jwts.builder()
            .subject(phoneNumber)
            .claim("userId", userId)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }

    fun getUserIdFromToken(token: String): UUID {
        val userId = getClaimsFromToken(token)["userId"] as String
        return UUID.fromString(userId)
    }

    private fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun getExpirationDateFromToken(token: String): Date {
        return getClaimsFromToken(token).expiration
    }
}