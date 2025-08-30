package com.anshtya.jetx.server.controller

import com.anshtya.jetx.server.dto.*
import com.anshtya.jetx.server.security.UserPrincipal
import com.anshtya.jetx.server.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegisterRequest
    ): ResponseEntity<ApiResponse<AuthResponse>> {
        val authResponse = authService.register(request)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "User registered successfully",
                data = authResponse
            )
        )
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): ResponseEntity<ApiResponse<AuthResponse>> {
        val authResponse = authService.login(request)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Login successful",
                data = authResponse
            )
        )
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @Valid @RequestBody request: RefreshTokenRequest
    ): ResponseEntity<ApiResponse<AuthResponse>> {
        val authResponse = authService.refreshToken(request)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Token refreshed successfully",
                data = authResponse
            )
        )
    }

    @PostMapping("/logout")
    fun logout(
        authentication: Authentication
    ): ResponseEntity<ApiResponse<Unit>> {
        val user = authentication.principal as UserPrincipal
        authService.logout(user.getId())
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Logout successful"
            )
        )
    }
}
