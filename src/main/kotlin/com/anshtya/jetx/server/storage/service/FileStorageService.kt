package com.anshtya.jetx.server.storage.service

import com.anshtya.jetx.server.storage.config.AppwriteProperties
import io.appwrite.ID
import io.appwrite.Role
import io.appwrite.models.InputFile
import io.appwrite.services.Storage
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileStorageService(
    private val storage: Storage,
    private val appwriteProperties: AppwriteProperties
) {
    suspend fun uploadToStorage(file: MultipartFile) {
        storage.createFile(
            bucketId = appwriteProperties.storageId,
            fileId = ID.unique(),
            file = InputFile.fromBytes(file.bytes),
            permissions = listOf(Role.any())
        )

    }

    suspend fun getFromStorage() {

    }

    private fun prepareFileUrl(fileId: String): String {
        return "${appwriteProperties.endpoint}/storage/buckets/${appwriteProperties.storageId}/files/$fileId/"
    }
}