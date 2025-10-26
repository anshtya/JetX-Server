package com.anshtya.jetx.server.storage.controller

import com.anshtya.jetx.server.storage.dto.FileUrlDto
import com.anshtya.jetx.server.storage.service.FileStorageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/storage")
class FileStorageController(
    private val fileStorageService: FileStorageService
) {
    @GetMapping("/upload")
    fun generateUploadUrl(
        @RequestParam("name") name: String,
        @RequestParam("contentType") contentType: String
    ): ResponseEntity<FileUrlDto> {
        val fileUrlDto = fileStorageService.generateUploadUrl(
            name = name,
            contentType = contentType
        )
        return ResponseEntity.ok(fileUrlDto)
    }

    @GetMapping("/{fileName}")
    fun generateDownloadUrl(
        @PathVariable fileName: String
    ): ResponseEntity<FileUrlDto> {
        val fileUrlDto = fileStorageService.generateDownloadUrl(fileName)
        return ResponseEntity.ok(fileUrlDto)
    }

    @DeleteMapping("/{fileName}")
    fun deleteFromStorage(
        @PathVariable fileName: String
    ): ResponseEntity<Unit> {
        fileStorageService.deleteFile(fileName)
        return ResponseEntity.noContent().build()
    }
}