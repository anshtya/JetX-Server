package com.anshtya.jetx.server.service

import com.anshtya.jetx.server.dto.*
import com.anshtya.jetx.server.entity.RefreshToken
import com.anshtya.jetx.server.entity.User
import com.anshtya.jetx.server.exception.UnauthorizedException
import com.anshtya.jetx.server.repository.RefreshTokenRepository
import com.anshtya.jetx.server.repository.UserRepository
import com.anshtya.jetx.server.security.JwtUtil
import jakarta.transaction.Transactional
import org.apache.coyote.BadRequestException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@Transactional
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val userService: UserService
) {

    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.existsByPhoneNumber(request.phoneNumber)) {
            throw BadRequestException("Phone number already registered")
        }

        if (userRepository.existsByUsername(request.username)) {
            throw BadRequestException("Username already taken")
        }

        val user = User(
            phoneNumber = request.phoneNumber,
            pinHash = passwordEncoder.encode(request.pin),
            username = request.username,
            displayName = request.displayName,
            fcmToken = request.fcmToken
        )

        val savedUser = userRepository.save(user)
        return generateAuthResponse(savedUser)
    }

    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByPhoneNumber(request.phoneNumber)
            ?: throw UnauthorizedException("Invalid credentials")

        if (!passwordEncoder.matches(request.pin, user.pinHash)) {
            throw UnauthorizedException("Invalid credentials")
        }

        // Update FCM token if provided
        request.fcmToken?.let {
            userRepository.updateFcmToken(user.id, it)
        }

        return generateAuthResponse(user)
    }

    fun refreshToken(request: RefreshTokenRequest): AuthResponse {
        val refreshToken = refreshTokenRepository.findByToken(request.refreshToken)
            ?: throw UnauthorizedException("Invalid refresh token")

        if (refreshToken.expiryDate.isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken)
            throw UnauthorizedException("Refresh token expired")
        }

        return generateAuthResponse(refreshToken.user)
    }

    fun logout(userId: Long) {
        refreshTokenRepository.deleteByUserId(userId)
        userService.updatePresence(userId, false)
    }

    private fun generateAuthResponse(user: User): AuthResponse {
        val accessToken = jwtUtil.generateAccessToken(user.id, user.username)
        val refreshTokenString = jwtUtil.generateRefreshToken(user.id)

        // Save refresh token
        refreshTokenRepository.deleteByUserId(user.id)
        val refreshToken = RefreshToken(
            token = refreshTokenString,
            user = user,
            expiryDate = LocalDateTime.now().plusDays(7)
        )
        refreshTokenRepository.save(refreshToken)

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshTokenString,
            user = user.toDto()
        )
    }
}
