package com.anshtya.jetx.server.userprofile.controller

import com.anshtya.jetx.server.security.JwtUtil
import com.anshtya.jetx.server.userprofile.dto.*
import com.anshtya.jetx.server.userprofile.service.UserProfileService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/user")
class UserProfileController(
    private val userProfileService: UserProfileService,
    private val jwtUtil: JwtUtil
) {
    @GetMapping("/{username}")
    fun getUserProfile(
        @PathVariable("username") username: String
    ): ResponseEntity<UserProfileDto> {
        val user = userProfileService.getUserProfileByUsername(username)
        return ResponseEntity.ok(user)
    }

    @PostMapping("/create")
    fun createUserProfile(
        request: HttpServletRequest,
        @Valid @RequestPart createProfileDto: CreateProfileDto,
        @RequestPart("file") profilePhoto: MultipartFile?
    ): ResponseEntity<Unit> {
        val userId = jwtUtil.getUserIdFromRequest(request)
        userProfileService.createUserProfile(userId, createProfileDto, profilePhoto)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/update")
    fun updateUserProfile(
        request: HttpServletRequest,
        @Valid @RequestBody updateProfileDto: UpdateProfileDto
    ): ResponseEntity<Unit> {
        val userId = jwtUtil.getUserIdFromRequest(request)
        userProfileService.updateUserProfile(userId, updateProfileDto)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/photo/update")
    fun updateProfilePhoto(
        request: HttpServletRequest,
        @RequestPart("profilePhoto") profilePhoto: MultipartFile
    ): ResponseEntity<Unit> {
        val userId = jwtUtil.getUserIdFromRequest(request)
        userProfileService.updateProfilePhoto(userId, profilePhoto)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/photo/remove")
    fun removeProfilePhoto(
        request: HttpServletRequest
    ): ResponseEntity<Unit> {
        val userId = jwtUtil.getUserIdFromRequest(request)
        userProfileService.removeProfilePhoto(userId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/fcm-token")
    fun updateFcmToken(
        request: HttpServletRequest,
        @RequestBody tokenRequest: FcmTokenRequestDto,
    ): ResponseEntity<Unit> {
        val userId = jwtUtil.getUserIdFromRequest(request)
        userProfileService.updateFcmToken(userId, tokenRequest)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/search/{query}")
    fun searchUserProfile(
        @PathVariable("query") query: String
    ): ResponseEntity<UserProfileSearchDto> {
        val searchResults = userProfileService.searchUserProfile(query)
        return ResponseEntity.ok(searchResults)
    }
}