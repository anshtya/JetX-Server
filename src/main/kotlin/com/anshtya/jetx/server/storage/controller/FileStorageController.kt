package com.anshtya.jetx.server.storage.controller

import com.anshtya.jetx.server.storage.service.FileStorageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/storage")
class FileStorageController(
    private val fileStorageService: FileStorageService
) {
    @PostMapping("/upload")
    suspend fun uploadToStorage(
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<Unit> {
        fileStorageService.uploadToStorage(file)
        return ResponseEntity.noContent().build()
    }
}