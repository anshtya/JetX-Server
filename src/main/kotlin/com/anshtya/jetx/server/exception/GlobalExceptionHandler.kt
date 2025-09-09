package com.anshtya.jetx.server.exception

import io.jsonwebtoken.JwtException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFoundException(
        ex: UsernameNotFoundException
    ): ResponseEntity<ErrorResult> {
        val errorResult = ErrorResult(
            HttpStatus.NOT_FOUND.value(),
            ex.message ?: "Phone number not found"
        )
        return ResponseEntity.status(errorResult.status)
            .body(errorResult)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(
        ex: AuthenticationException
    ): ResponseEntity<ErrorResult> {
        val errorResult = ErrorResult(
            HttpStatus.UNAUTHORIZED.value(),
            ex.message ?: "Authentication failed"
        )
        return ResponseEntity.status(errorResult.status)
            .body(errorResult)
    }

    @ExceptionHandler(JwtException::class)
    fun handleJwtException(
        ex: JwtException
    ): ResponseEntity<ErrorResult> {
        val errorResult = ErrorResult(
            HttpStatus.UNAUTHORIZED.value(),
            ex.message ?: "Invalid JWT Token"
        )
        return ResponseEntity.status(errorResult.status)
            .body(errorResult)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(
        ex: NotFoundException
    ): ResponseEntity<ErrorResult> {
        val errorResult = ErrorResult(
            HttpStatus.NOT_FOUND.value(),
            ex.message ?: "Requested resource was not found"
        )
        return ResponseEntity.status(errorResult.status)
            .body(errorResult)
    }

    @ExceptionHandler(UsernameAlreadyExistsException::class)
    fun handleUsernameAlreadyExistsException(): ResponseEntity<ErrorResult> {
        val errorResult = ErrorResult(
            HttpStatus.CONFLICT.value(),
            "Username already taken"
        )
        return ResponseEntity.status(errorResult.status)
            .body(errorResult)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<ErrorResult> {
        val errors = ex.bindingResult.fieldErrors.joinToString { error ->
            "${error.field} - ${error.defaultMessage}"
        }
        val errorResult = ErrorResult(
            HttpStatus.BAD_REQUEST.value(),
            errors
        )
        return ResponseEntity.status(errorResult.status)
            .body(errorResult)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        ex: Exception
    ): ResponseEntity<ErrorResult> {
        val errorResult = ErrorResult(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ex.message ?: "An unexpected error occurred"
        )
        return ResponseEntity.status(errorResult.status)
            .body(errorResult)
    }
}