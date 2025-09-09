package com.anshtya.jetx.server.userprofile.controller

import com.anshtya.jetx.server.userprofile.dto.CreateProfileDto
import com.anshtya.jetx.server.userprofile.dto.UpdateProfileDto
import com.anshtya.jetx.server.userprofile.dto.UserProfileDto
import com.anshtya.jetx.server.userprofile.dto.UserProfileSearchDto
import com.anshtya.jetx.server.userprofile.service.UserProfileService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserProfileController(
    private val userProfileService: UserProfileService
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
        @Valid @RequestBody createProfileDto: CreateProfileDto
    ): ResponseEntity<Unit> {
        userProfileService.createUserProfile(request, createProfileDto)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/update")
    fun updateUserProfile(
        request: HttpServletRequest,
        @Valid @RequestBody updateProfileDto: UpdateProfileDto
    ): ResponseEntity<Unit> {
        userProfileService.updateUserProfile(request, updateProfileDto)
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