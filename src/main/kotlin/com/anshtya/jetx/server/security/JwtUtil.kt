package com.anshtya.jetx.server.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil(
    @param:Value($$"${app.jwt.secret}")
    private val jwtSecret: String,

    @param:Value($$"${app.jwt.access-token-expiration}")
    private val accessTokenExpiration: Long,

    @param:Value($$"${app.jwt.refresh-token-expiration}")
    private val refreshTokenExpiration: Long
) {

    private val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))

    fun generateAccessToken(userId: Long, username: String): String {
        return createToken(mapOf("userId" to userId, "username" to username), accessTokenExpiration)
    }

    fun generateRefreshToken(userId: Long): String {
        return createToken(mapOf("userId" to userId), refreshTokenExpiration)
    }

    private fun createToken(claims: Map<String, Any>, expiration: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)

        return Jwts.builder()
            .claims(claims)
            .issuedAt(now)
            .expiration(expiryDate)
//            .signWith(key, SignatureAlgorithm.HS512)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getUserIdFromToken(token: String): Long {
        val claims = getClaimsFromToken(token)
        return claims["userId"].toString().toLong()
    }

    fun getUsernameFromToken(token: String): String {
        val claims = getClaimsFromToken(token)
        return claims["username"].toString()
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