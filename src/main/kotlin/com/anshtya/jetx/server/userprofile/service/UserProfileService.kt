package com.anshtya.jetx.server.userprofile.service

import com.anshtya.jetx.server.auth.repository.AuthUserRepository
import com.anshtya.jetx.server.exception.NotFoundException
import com.anshtya.jetx.server.storage.dto.FileUrlDto
import com.anshtya.jetx.server.storage.service.FileStorageService
import com.anshtya.jetx.server.userprofile.dto.*
import com.anshtya.jetx.server.userprofile.entity.UserProfile
import com.anshtya.jetx.server.userprofile.repository.UserProfileRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrElse

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
        val userId = UUID.fromString(getProfileRequestDto.userId)
        val phoneNumber = authUserRepository.findById(userId).orElseThrow {
            IllegalStateException("User doesn't exist")
        }.phoneNumber
        return userProfileRepository.findById(userId).getOrElse {
            throw NotFoundException("User not found")
        }.toDto(phoneNumber)
    }

    fun createUserProfile(
        userId: UUID,
        createProfileDto: CreateProfileDto
    ): UserProfileDto {
        val user = authUserRepository.findById(userId).orElseThrow {
            IllegalStateException("User doesn't exist")
        }

        val userProfile = UserProfile(
            username = createProfileDto.username,
            user = user,
            displayName = createProfileDto.displayName,
            photoExists = createProfileDto.photoExists,
            fcmToken = createProfileDto.fcmToken
        )
        userProfileRepository.save(userProfile)

        return userProfile.toDto(user.phoneNumber)
    }

    fun checkUsername(
        usernameDto: UsernameDto
    ): CheckUsernameResponseDto {
        val userProfileExists = userProfileRepository.existsByUsername(usernameDto.username)
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

    fun getDownloadProfilePhotoUrl(
        userId: UUID
    ): FileUrlDto {
        getUserProfile(userId)

        return storageService.generateDownloadUserProfilePhotoUrl(userId.toString())
    }

    fun getUploadProfilePhotoUrl(
        userId: UUID,
        contentType: String
    ): FileUrlDto {
        val userProfile = getUserProfile(userId)

        storageService.deleteUserProfilePhoto(userId.toString())
        val fileUrlDto = storageService.generateUploadUserProfilePhotoUrl(
            name = userId.toString(),
            contentType = contentType
        )
        userProfileRepository.save(userProfile.copy(photoExists = true))
        return fileUrlDto
    }

    fun removeProfilePhoto(
        userId: UUID
    ) {
        val existingProfile = getUserProfile(userId)
        storageService.deleteUserProfilePhoto(userId.toString())
        userProfileRepository.save(existingProfile.copy(photoExists = false))
    }

    fun updateUsername(
        userId: UUID,
        usernameDto: UsernameDto
    ) {
        val existingProfile = getUserProfile(userId)
        val updatedProfile = existingProfile.copy(
            username = usernameDto.username
        )
        userProfileRepository.save(updatedProfile)
    }

    fun updateDisplayName(
        userId: UUID,
        displayNameDto: DisplayNameDto
    ) {
        val existingProfile = getUserProfile(userId)
        val updatedProfile = existingProfile.copy(
            displayName = displayNameDto.name
        )
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
        val now = Instant.now()
        userProfileRepository.updateUserPresence(userId, isOnline, now)
    }

    fun getUserProfile(id: UUID): UserProfile {
        return userProfileRepository.findById(id).getOrElse {
            throw IllegalStateException("User profile doesn't exist")
        }
    }
}