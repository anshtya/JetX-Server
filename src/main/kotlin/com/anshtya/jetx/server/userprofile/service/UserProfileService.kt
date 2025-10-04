package com.anshtya.jetx.server.userprofile.service

import com.anshtya.jetx.server.auth.repository.AuthUserRepository
import com.anshtya.jetx.server.exception.NotFoundException
import com.anshtya.jetx.server.exception.UsernameAlreadyExistsException
import com.anshtya.jetx.server.storage.service.FileStorageService
import com.anshtya.jetx.server.userprofile.dto.*
import com.anshtya.jetx.server.userprofile.entity.UserProfile
import com.anshtya.jetx.server.userprofile.repository.UserProfileRepository
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
    private val storageService: FileStorageService
) {
    fun getUserProfileByUsername(username: String): UserProfileDto {
        return userProfileRepository.findByUsername(username)?.toDto()
            ?: throw NotFoundException("User not found")
    }

    fun getUserProfileById(
        getProfileRequestDto: GetProfileRequestDto
    ): UserProfileDto {
        return userProfileRepository.findByUserId(UUID.fromString(getProfileRequestDto.userId))?.toDto()
            ?: throw NotFoundException("User not found")
    }

    fun createUserProfile(
        userId: UUID,
        createProfileDto: CreateProfileDto,
        profilePhoto: MultipartFile?
    ) {
        val user = authUserRepository.findById(userId).orElseThrow {
            IllegalStateException("User doesn't exist")
        }

        // TODO: implement profile storage
//        var profilePhotoFileName: String? = null
//        if (profilePhoto != null && !profilePhoto.isEmpty) {
//            profilePhotoFileName = uploadProfilePhoto(profilePhoto, createProfileDto.username)
//        }

        val userProfile = UserProfile(
            username = createProfileDto.username,
            user = user,
            displayName = createProfileDto.displayName,
            profilePhotoKey = null,
            fcmToken = createProfileDto.fcmToken
        )
        userProfileRepository.save(userProfile)
    }

    fun checkUsername(
        checkUsernameDto: CheckUsernameDto
    ): CheckUsernameResponseDto {
        val userProfileExists = userProfileRepository.findByUsername(checkUsernameDto.username) != null
        return if (userProfileExists) {
            CheckUsernameResponseDto(
                valid = false,
                message = "User already exists."
            )
        } else {
            CheckUsernameResponseDto(
                valid = true,
                message = null
            )
        }
    }

    fun updateUserProfile(
        userId: UUID,
        updateProfileDto: UpdateProfileDto
    ) {
        val userProfile = getUserProfile(userId)

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

    fun updateProfilePhoto(
        userId: UUID,
        profilePhoto: MultipartFile
    ) {
        val existingProfile = getUserProfile(userId)

        existingProfile.profilePhotoKey?.let { key ->
            storageService.deleteFile(key)
        }

        val newPhotoFileName = uploadProfilePhoto(profilePhoto, existingProfile.username)

        val updatedProfile = existingProfile.copy(
            profilePhotoKey = newPhotoFileName
        )

        userProfileRepository.save(updatedProfile)
    }

    fun removeProfilePhoto(
        userId: UUID
    ) {
        val existingProfile = getUserProfile(userId)

        existingProfile.profilePhotoKey?.let { key ->
            storageService.deleteFile(key)
        }

        val updatedProfile = existingProfile.copy(profilePhotoKey = null)

        userProfileRepository.save(updatedProfile)
    }

    fun updateFcmToken(
        userId: UUID,
        tokenRequest: FcmTokenRequestDto
    ) {
        val existingProfile = getUserProfile(userId)

        val updatedProfile = existingProfile.copy(fcmToken = tokenRequest.token)

        userProfileRepository.save(updatedProfile)
    }

    fun searchUserProfile(query: String): UserProfileSearchDto {
        val searchResults = userProfileRepository.searchUsers(query).map { it.toDto() }
        return UserProfileSearchDto(searchResults)
    }

    fun updatePresence(userId: UUID, isOnline: Boolean) {
        val now = LocalDateTime.now()
        userProfileRepository.updateUserPresence(userId, isOnline, now)
    }

    private fun getUserProfile(id: UUID): UserProfile {
        return userProfileRepository.findByUserId(id)
            ?: throw IllegalStateException("User profile doesn't exist")
    }

    private fun uploadProfilePhoto(
        profilePhoto: MultipartFile,
        username: String
    ): String {
        val fileName = storageService.uploadFile(profilePhoto)
        return "${username}-${fileName}"
    }
}