package com.anshtya.jetx.server.storage.controller

import com.anshtya.jetx.server.storage.dto.GetFileDto
import com.anshtya.jetx.server.storage.service.FileStorageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
        fileStorageService.uploadFile(file)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{fileName}/download")
    fun getDownloadUrl(
        @PathVariable fileName: String
    ): ResponseEntity<GetFileDto> {
        val file = fileStorageService.getFileDownloadUrl(fileName)
        return ResponseEntity.ok(
            GetFileDto(
                url = file.url
            )
        )
    }

    @DeleteMapping("/{fileName}/delete")
    fun deleteFromStorage(
        @PathVariable fileName: String
    ): ResponseEntity<Map<String, Any>> {
        fileStorageService.deleteFile(fileName)
        return ResponseEntity.noContent().build()
    }
}