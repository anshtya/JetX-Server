package com.anshtya.jetx.server.service

import com.anshtya.jetx.server.dto.UpdateProfileRequest
import com.anshtya.jetx.server.dto.UserDto
import com.anshtya.jetx.server.dto.toDto
import com.anshtya.jetx.server.entity.User
import com.anshtya.jetx.server.exception.BadRequestException
import com.anshtya.jetx.server.exception.NotFoundException
import com.anshtya.jetx.server.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
//    private val fileStorageService: FileStorageService,
//    private val redisTemplate: RedisTemplate<String, String>
) {

    fun getUserById(id: Long): User {
        return userRepository.findById(id).orElseThrow {
            NotFoundException("User not found")
        }
    }

    fun getUserDto(id: Long): UserDto {
        return getUserById(id).toDto()
    }

    fun updateProfile(userId: Long, request: UpdateProfileRequest): UserDto {
        val user = getUserById(userId)

        // Check if username is already taken by another user
        request.username?.let { newUsername ->
            if (newUsername != user.username && userRepository.existsByUsername(newUsername)) {
                throw BadRequestException("Username already taken")
            }
        }

        val updatedUser = user.copy(
            username = request.username ?: user.username,
            displayName = request.displayName ?: user.displayName
        )

        return userRepository.save(updatedUser).toDto()
    }

    fun uploadProfilePhoto(userId: Long, file: MultipartFile): UserDto {
        val user = getUserById(userId)
//        val photoUrl = fileStorageService.storeFile(file, "profile-photos")

        val updatedUser = user.copy(profilePhotoUrl = "df")
        return userRepository.save(updatedUser).toDto()
    }

    fun searchUsers(query: String): List<UserDto> {
        return userRepository.searchUsers(query).map { it.toDto() }
    }

    fun updatePresence(userId: Long, isOnline: Boolean) {
        val now = LocalDateTime.now()
        userRepository.updateUserPresence(userId, isOnline, now)

        // Cache presence in Redis
        val key = "user:presence:$userId"
//        if (isOnline) {
//            redisTemplate.opsForValue().set(key, "online", 5, TimeUnit.MINUTES)
//        } else {
//            redisTemplate.delete(key)
//        }
    }

//    fun isUserOnline(userId: Long): Boolean {
//        val key = "user:presence:$userId"
//        return redisTemplate.hasKey(key)
//    }
}