package com.anshtya.jetx.server.hello.controller

import com.anshtya.jetx.server.hello.dto.HelloResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/hello")
    fun hello(): ResponseEntity<HelloResponseDto> {
        return ResponseEntity.ok(
            HelloResponseDto(message = "Server is running")
        )
    }
}
