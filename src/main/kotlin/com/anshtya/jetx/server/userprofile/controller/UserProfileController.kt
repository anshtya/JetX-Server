package com.anshtya.jetx.server.userprofile.controller

import com.anshtya.jetx.server.security.JwtUtil
import com.anshtya.jetx.server.storage.dto.FileUrlDto
import com.anshtya.jetx.server.userprofile.dto.*
import com.anshtya.jetx.server.userprofile.service.UserProfileService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @PostMapping("/get")
    fun getUserProfile(
        @RequestBody getProfileRequestDto: GetProfileRequestDto
    ): ResponseEntity<UserProfileDto> {
        val user = userProfileService.getUserProfileById(getProfileRequestDto)
        return ResponseEntity.ok(user)
    }

    @PostMapping("/create")
    fun createUserProfile(
        request: HttpServletRequest,
        @Valid @RequestBody createProfileDto: CreateProfileDto,
    ): ResponseEntity<UserProfileDto> {
        val userId = jwtUtil.getUserIdFromRequest(request)
        val userProfile = userProfileService.createUserProfile(userId, createProfileDto)
        return ResponseEntity.ok(userProfile)
    }

    @PostMapping("/check_username")
    fun checkUsername(
        @Valid @RequestBody usernameDto: UsernameDto
    ): ResponseEntity<CheckUsernameResponseDto> {
        val response = userProfileService.checkUsername(usernameDto)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/photo/download")
    fun getDownloadProfilePhotoUrl(
        request: HttpServletRequest
    ): ResponseEntity<FileUrlDto> {
        val userId = jwtUtil.getUserIdFromRequest(request)
        val fileUrl = userProfileService.getDownloadProfilePhotoUrl(userId)
        return ResponseEntity.ok(fileUrl)
    }

    @GetMapping("/photo/upload")
    fun getUploadProfilePhotoUrl(
        request: HttpServletRequest,
        @RequestParam("contentType") contentType: String
    ): ResponseEntity<FileUrlDto> {
        val userId = jwtUtil.getUserIdFromRequest(request)
        val fileUrl = userProfileService.getUploadProfilePhotoUrl(userId, contentType)
        return ResponseEntity.ok(fileUrl)
    }

    @PatchMapping("/photo/remove")
    fun removeProfilePhoto(
        request: HttpServletRequest,
    ): ResponseEntity<Unit> {
        val userId = jwtUtil.getUserIdFromRequest(request)
        userProfileService.removeProfilePhoto(userId)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/name/update")
    fun updateDisplayName(
        request: HttpServletRequest,
        @Valid @RequestBody displayNameDto: DisplayNameDto
    ): ResponseEntity<Unit> {
        val userId = jwtUtil.getUserIdFromRequest(request)
        userProfileService.updateDisplayName(userId, displayNameDto)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/username/update")
    fun updateUsername(
        request: HttpServletRequest,
        @Valid @RequestBody usernameDto: UsernameDto
    ): ResponseEntity<Unit> {
        val userId = jwtUtil.getUserIdFromRequest(request)
        userProfileService.updateUsername(userId, usernameDto)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/fcm/update")
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