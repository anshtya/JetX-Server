package com.anshtya.jetx.server.userprofile.service

import com.anshtya.jetx.server.auth.repository.AuthUserRepository
import com.anshtya.jetx.server.exception.NotFoundException
import com.anshtya.jetx.server.exception.UsernameAlreadyExistsException
import com.anshtya.jetx.server.security.JwtUtil
import com.anshtya.jetx.server.userprofile.dto.*
import com.anshtya.jetx.server.userprofile.entity.UserProfile
import com.anshtya.jetx.server.userprofile.repository.UserProfileRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
class UserProfileService(
    private val userProfileRepository: UserProfileRepository,
    private val authUserRepository: AuthUserRepository,
    private val jwtUtil: JwtUtil
) {
    fun getUserProfileByUsername(username: String): UserProfileDto {
        return userProfileRepository.findByUsername(username)?.toDto()
            ?: throw NotFoundException("User not found")
    }

    fun createUserProfile(
        request: HttpServletRequest,
        createProfileDto: CreateProfileDto
    ) {
        val token = jwtUtil.getToken(request)
        val userId = jwtUtil.getUserIdFromToken(token)
        val user = authUserRepository.findById(userId).orElseThrow {
            IllegalStateException("User doesn't exist")
        }

        val userProfileExists = userProfileRepository.findByUsername(createProfileDto.username) != null
        if (userProfileExists) {
            throw IllegalStateException("User profile already exists")
        }

        val userProfile = UserProfile(
            username = createProfileDto.username,
            user = user,
            displayName = createProfileDto.displayName,
            profilePhotoUrl = createProfileDto.profilePhotoUrl
        )
        userProfileRepository.save(userProfile)
    }

    fun updateUserProfile(
        request: HttpServletRequest,
        updateProfileDto: UpdateProfileDto
    ) {
        val token = jwtUtil.getToken(request)
        val userId = jwtUtil.getUserIdFromToken(token)

        val userProfile = userProfileRepository.findByUserId(userId)
            ?: throw IllegalStateException("Profile doesn't exist")

        // Check if username is already taken by another user
        updateProfileDto.username?.let { newUsername ->
            if (newUsername != userProfile.username && userProfileRepository.existsByUsername(newUsername)) {
                throw UsernameAlreadyExistsException()
            }
        }

        userProfile.apply {
            updateProfileDto.username?.let { username = it }
            updateProfileDto.displayName?.let { displayName = it }
        }

        userProfileRepository.save(userProfile)
    }

    fun uploadProfilePhoto(userId: UUID, file: MultipartFile) {
        // TODO: To be implemented
    }

    fun searchUserProfile(query: String): UserProfileSearchDto {
        val searchResults = userProfileRepository.searchUsers(query).map { it.toDto() }
        return UserProfileSearchDto(searchResults)
    }

    fun updatePresence(userId: UUID, isOnline: Boolean) {
        val now = LocalDateTime.now()
        userProfileRepository.updateUserPresence(userId, isOnline, now)
    }
}