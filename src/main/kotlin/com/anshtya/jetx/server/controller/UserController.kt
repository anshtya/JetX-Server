package com.anshtya.jetx.server.controller

import com.anshtya.jetx.server.dto.ApiResponse
import com.anshtya.jetx.server.dto.UserDto
import com.anshtya.jetx.server.dto.toDto
import com.anshtya.jetx.server.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userRepository: UserRepository
) {
    @GetMapping("/{id}")
    fun getUser(
        @PathVariable("id") id: Long
    ): ResponseEntity<ApiResponse<UserDto>?> {
        val user = userRepository.findById(1).get().toDto()
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "done",
                data = user,
            )
        )
    }
}