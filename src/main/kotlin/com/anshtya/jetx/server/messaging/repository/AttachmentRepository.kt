package com.anshtya.jetx.server.messaging.repository

import com.anshtya.jetx.server.messaging.entity.Attachment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AttachmentRepository: JpaRepository<Attachment, UUID>