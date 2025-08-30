package com.anshtya.jetx.server.service

//import org.springframework.stereotype.Service
//
//@Service
//class FileStorageService(
//    private val attachmentRepository: AttachmentRepository,
//    private val userService: UserService,
//    @Value("\${app.storage.upload-dir}")
//    private val uploadDir: String,
//    @Value("\${app.storage.base-url}")
//    private val baseUrl: String
//) {
//
//    private val uploadPath: Path = Paths.get(uploadDir)
//
//    init {
//        try {
//            Files.createDirectories(uploadPath)
//        } catch (ex: Exception) {
//            throw RuntimeException("Could not create upload directory!", ex)
//        }
//    }
//
//    fun storeFile(file: MultipartFile, subDir: String): String {
//        val filename = generateFilename(file.originalFilename)
//        val targetLocation = uploadPath.resolve(subDir).resolve(filename)
//
//        try {
//            Files.createDirectories(targetLocation.parent)
//            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
//        } catch (ex: IOException) {
//            throw RuntimeException("Could not store file $filename", ex)
//        }
//
//        return "$baseUrl/api/attachments/download/$subDir/$filename"
//    }
//
//    fun storeAttachment(file: MultipartFile, userId: Long): Attachment {
//        validateFile(file)
//
//        val user = userService.getUserById(userId)
//        val filename = generateFilename(file.originalFilename)
//        val filePath = "attachments/$filename"
//        val targetLocation = uploadPath.resolve(filePath)
//
//        try {
//            Files.createDirectories(targetLocation.parent)
//            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
//        } catch (ex: IOException) {
//            throw RuntimeException("Could not store attachment", ex)
//        }
//
//        val attachment = Attachment(
//            filename = filename,
//            originalFilename = file.originalFilename ?: filename,
//            filePath = filePath,
//            mimeType = file.contentType ?: "application/octet-stream",
//            fileSize = file.size,
//            uploadedBy = user,
//            expiryDate = LocalDateTime.now().plusDays(14) // 2 weeks retention
//        )
//
//        return attachmentRepository.save(attachment)
//    }
//
//    fun getFilePath(filename: String): Path {
//        return uploadPath.resolve(filename).normalize()
//    }
//
//    private fun generateFilename(originalFilename: String?): String {
//        val extension = originalFilename?.substringAfterLast('.', "") ?: ""
//        val uuid = UUID.randomUUID().toString()
//        return if (extension.isNotEmpty()) "$uuid.$extension" else uuid
//    }
//
//    private fun validateFile(file: MultipartFile) {
//        if (file.isEmpty) {
//            throw BadRequestException("File is empty")
//        }
//
//        val maxSize = 50 * 1024 * 1024 // 50MB
//        if (file.size > maxSize) {
//            throw BadRequestException("File size exceeds maximum limit (50MB)")
//        }
//
//        val allowedTypes = setOf(
//            "image/jpeg", "image/png", "image/gif",
//            "audio/mpeg", "audio/wav", "audio/ogg",
//            "video/mp4", "video/mpeg", "video/quicktime",
//            "application/pdf", "application/msword",
//            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
//        )
//
//        if (file.contentType !in allowedTypes) {
//            throw BadRequestException("File type not supported: ${file.contentType}")
//        }
//    }
//}