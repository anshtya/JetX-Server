package com.anshtya.jetx.server.storage.service

import com.anshtya.jetx.server.storage.config.B2Properties
import com.anshtya.jetx.server.storage.dto.FileUrlDto
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration

@Service
class FileStorageService(
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner,
    private val b2Properties: B2Properties
) {
    fun generateUploadUrl(
        name: String,
        contentType: String
    ): FileUrlDto {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(b2Properties.bucketName)
            .key(name)
            .contentType(contentType)
            .build()

        val preSignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofHours(1))
            .putObjectRequest(putObjectRequest)
            .build()

        val presignedPutObjectRequest = s3Presigner.presignPutObject(preSignRequest)
        return FileUrlDto(presignedPutObjectRequest.url().toString())
    }

    fun generateDownloadUrl(
        fileName: String
    ): FileUrlDto {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(b2Properties.bucketName)
            .key(fileName)
            .build()

        val presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofDays(7))
            .getObjectRequest(getObjectRequest)
            .build()

        val presignedRequest = s3Presigner.presignGetObject(presignRequest)
        return FileUrlDto(presignedRequest.url().toString())
    }

    fun deleteFile(
        fileName: String
    ) {
        val deleteRequest = DeleteObjectRequest.builder()
            .bucket(b2Properties.bucketName)
            .key(fileName)
            .build()

        s3Client.deleteObject(deleteRequest)
    }

    fun deleteUserProfilePhoto(
        fileName: String
    ) {
        deleteFile("profile/$fileName")
    }

    fun generateDownloadUserProfilePhotoUrl(
        name: String
    ): FileUrlDto {
        return generateDownloadUrl(
            fileName = "profile/$name"
        )
    }

    fun generateUploadUserProfilePhotoUrl(
        name: String,
        contentType: String
    ): FileUrlDto {
        return generateUploadUrl(
            name = "profile/$name",
            contentType = contentType
        )
    }
}