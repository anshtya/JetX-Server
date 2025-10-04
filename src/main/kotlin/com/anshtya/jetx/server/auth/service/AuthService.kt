package com.anshtya.jetx.server.auth.service

import com.anshtya.jetx.server.auth.dto.*
import com.anshtya.jetx.server.auth.entity.AuthUser
import com.anshtya.jetx.server.auth.entity.RefreshToken
import com.anshtya.jetx.server.auth.repository.AuthUserRepository
import com.anshtya.jetx.server.auth.repository.RefreshTokenRepository
import com.anshtya.jetx.server.security.JwtUtil
import com.anshtya.jetx.server.userprofile.repository.UserProfileRepository
import com.anshtya.jetx.server.userprofile.service.UserProfileService
import jakarta.transaction.Transactional
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
@Transactional
class AuthService(
    private val userProfileRepository: UserProfileRepository,
    private val authUserRepository: AuthUserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val userProfileService: UserProfileService
) {
    fun register(request: RegisterDto): AuthResultDto {
        if (authUserRepository.existsByPhoneNumber(request.phoneNumber)) {
            throw IllegalStateException("Phone number already registered")
        }

        val user = AuthUser(
            phoneNumber = request.phoneNumber,
            pinHash = passwordEncoder.encode(request.pin)
        )

        val savedUser = authUserRepository.save(user)
        return generateAuthResponse(savedUser)
    }

    fun logIn(request: LogInDto): AuthResultDto {
        val user = authUserRepository.findByPhoneNumber(request.phoneNumber)
            ?: throw UsernameNotFoundException("Invalid credentials")

        if (!passwordEncoder.matches(request.pin, user.pinHash)) {
            throw BadCredentialsException("Invalid credentials")
        }

        userProfileRepository.updateFcmToken(user.id!!, request.fcmToken)

        return generateAuthResponse(user)
    }

    fun checkUser(phoneNumber: String): Boolean {
        return authUserRepository.existsByPhoneNumber(phoneNumber)
    }

    fun refreshToken(request: RefreshDto): AuthResultDto {
        val refreshToken = refreshTokenRepository.findByToken(request.refreshToken)
            ?: throw BadCredentialsException("Invalid refresh token")

        return generateAuthResponse(refreshToken.user)
    }

    fun logout(logOutDto: LogOutDto) {
        val userId = jwtUtil.getUserIdFromToken(logOutDto.token)
        refreshTokenRepository.deleteByUserId(userId)
        userProfileService.updatePresence(userId, false)
    }

    private fun generateAuthResponse(authUser: AuthUser): AuthResultDto {
        val accessToken = jwtUtil.generateAccessToken(authUser.phoneNumber, authUser.id!!)
        val refreshTokenString = jwtUtil.generateRefreshToken(authUser.phoneNumber, authUser.id)

        // Save refresh token
        refreshTokenRepository.deleteByUserId(authUser.id)
        val refreshToken = RefreshToken(
            token = refreshTokenString,
            user = authUser
        )
        refreshTokenRepository.save(refreshToken)

        return AuthResultDto(
            userId = authUser.id,
            accessToken = accessToken,
            refreshToken = refreshTokenString
        )
    }
}