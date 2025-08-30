package com.anshtya.jetx.server.exception

import com.anshtya.jetx.server.dto.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<ErrorResponse> {
        logger.warn("Not found: ${ex.message}")
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(message = ex.message ?: "Resource not found"))
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException): ResponseEntity<ErrorResponse> {
        logger.warn("Bad request: ${ex.message}")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(message = ex.message ?: "Bad request"))
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(ex: UnauthorizedException): ResponseEntity<ErrorResponse> {
        logger.warn("Unauthorized: ${ex.message}")
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(message = ex.message ?: "Unauthorized"))
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbiddenException(ex: ForbiddenException): ResponseEntity<ErrorResponse> {
        logger.warn("Forbidden: ${ex.message}")
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ErrorResponse(message = ex.message ?: "Forbidden"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.map { error: FieldError ->
            "${error.field}: ${error.defaultMessage}"
        }

        logger.warn("Validation failed: $errors")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(
                message = "Validation failed",
                errors = errors
            ))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected error", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(message = "Internal server error"))
    }
}