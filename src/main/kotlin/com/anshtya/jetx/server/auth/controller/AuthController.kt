package com.anshtya.jetx.server.auth.controller

import com.anshtya.jetx.server.auth.dto.*
import com.anshtya.jetx.server.auth.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/check")
    fun checkUser(
        @Valid @RequestBody body: CheckUserDto
    ): ResponseEntity<CheckUserResponseDto> {
        val userExists = authService.checkUser(body.phoneNumber)
        return ResponseEntity.ok(
            CheckUserResponseDto(userExists)
        )
    }

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegisterDto
    ): ResponseEntity<AuthResultDto> {
        val authResult = authService.register(request)
        return ResponseEntity.ok(authResult)
    }

    @PostMapping("/login")
    fun logIn(
        @Valid @RequestBody request: LogInDto
    ): ResponseEntity<AuthResultDto> {
        val authResult = authService.logIn(request)
        return ResponseEntity.ok(authResult)
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @Valid @RequestBody request: RefreshDto
    ): ResponseEntity<AuthResultDto> {
        val authResult = authService.refreshToken(request)
        return ResponseEntity.ok(authResult)
    }

    @PostMapping("/logout")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @Valid @RequestBody logOutDto: LogOutDto
    ): ResponseEntity<Unit> {
        authService.logout(logOutDto)
        val authentication = SecurityContextHolder.getContext().authentication
        SecurityContextLogoutHandler().logout(request, response, authentication)
        return ResponseEntity
            .noContent()
            .build()
    }
}