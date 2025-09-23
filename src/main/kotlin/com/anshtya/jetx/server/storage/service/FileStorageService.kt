package com.anshtya.jetx.server.storage.service

import com.anshtya.jetx.server.storage.config.B2Properties
import com.anshtya.jetx.server.storage.dto.GetFileDto
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import java.time.Duration

@Service
class FileStorageService(
    private val s3Client: S3Client,
    private val preSigner: S3Presigner,
    private val b2Properties: B2Properties
) {
    fun uploadFile(
        file: MultipartFile
    ): String? {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(b2Properties.bucketName)
            .key(file.originalFilename)
            .contentType(file.contentType)
            .contentLength(file.size)
            .build()

        s3Client.putObject(
            putObjectRequest, RequestBody.fromInputStream(file.inputStream, file.size)
        )

        return file.originalFilename!!
    }

    fun getFileDownloadUrl(
        fileName: String
    ): GetFileDto {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(b2Properties.bucketName)
            .key(fileName)
            .build()

        val presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofDays(7))
            .getObjectRequest(getObjectRequest)
            .build()

        val presignedRequest = preSigner.presignGetObject(presignRequest)
        return GetFileDto(
            url = presignedRequest.url().toString()
        )
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
}