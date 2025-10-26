package com.anshtya.jetx.server.messaging.service

import com.anshtya.jetx.server.firebase.service.FirebaseService
import com.anshtya.jetx.server.messaging.dto.MessageDto
import com.anshtya.jetx.server.messaging.dto.MessageUpdateDto
import com.anshtya.jetx.server.messaging.dto.toDto
import com.anshtya.jetx.server.messaging.dto.toStringMap
import com.anshtya.jetx.server.messaging.entity.Message
import com.anshtya.jetx.server.messaging.entity.MessageType
import com.anshtya.jetx.server.messaging.repository.AttachmentRepository
import com.anshtya.jetx.server.messaging.repository.MessageRepository
import com.anshtya.jetx.server.userprofile.service.UserProfileService
import com.anshtya.jetx.server.websocket.WebSocketHandler
import com.anshtya.jetx.server.websocket.dto.MessageStatusUpdate
import com.anshtya.jetx.server.websocket.dto.WebSocketMessageDto
import com.anshtya.jetx.server.websocket.dto.WebSocketMessageType
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class MessageService(
    private val messageRepository: MessageRepository,
    private val attachmentRepository: AttachmentRepository,
    private val groupService: GroupService,
    private val userProfileService: UserProfileService,
    private val firebaseService: FirebaseService,
    private val webSocketHandler: WebSocketHandler
) {
    fun getMessage(
        id: UUID
    ): MessageDto {
        return messageRepository.findById(id).get().toDto()
    }

    fun saveMessage(
        messageDto: MessageDto
    ) {
        val sender = userProfileService.getUserProfile(messageDto.senderId)

        val message = when (messageDto.type) {
            MessageType.INDIVIDUAL -> Message.createIndividualMessage(
                id = messageDto.id,
                sender = sender,
                receiver = userProfileService.getUserProfile(messageDto.targetId),
                content = messageDto.content,
                attachment = messageDto.attachmentId?.let {
                    attachmentRepository.findById(it).get()
                }
            )

            MessageType.GROUP -> Message.createGroupMessage(
                id = messageDto.id,
                sender = sender,
                group = groupService.getGroup(messageDto.targetId),
                content = messageDto.content,
                attachment = messageDto.attachmentId?.let {
                    attachmentRepository.findById(it).get()
                }
            )
        }
        val savedMessage = messageRepository.save(message).toDto()

        when (messageDto.type) {
            MessageType.INDIVIDUAL -> {
                firebaseService.sendDataMessage(
                    token = userProfileService.getFcmTokenByUserId(savedMessage.targetId),
                    data = savedMessage.toStringMap()
                )
            }

            MessageType.GROUP -> {
                firebaseService.sendMulticastDataMessage(
                    token = groupService.getUserTokens(messageDto.targetId),
                    data = savedMessage.toStringMap()
                )
            }
        }
    }

    fun updateMessagesReceived(
        dto: MessageUpdateDto
    ) {
        messageRepository.updateMessageReceived(dto.messageIds)
        dto.messageIds.forEach { messageId ->
            val message = messageRepository.findById(messageId).get()
            val senderId = message.sender.id!!
            webSocketHandler.sendToUser(
                userId = senderId,
                data = WebSocketMessageDto(
                    type = WebSocketMessageType.MESSAGE_UPDATE,
                    data = MessageStatusUpdate(
                        id = message.id,
                        received = true,
                        seen = false
                    )
                ),
            )
        }
    }

    fun updateMessageReceived(
        id: UUID
    ) {
        val message = messageRepository.findById(id).get()
        val senderId = message.sender.id!!
        messageRepository.updateMessageReceived(id)
        webSocketHandler.sendToUser(
            userId = senderId,
            data = WebSocketMessageDto(
                type = WebSocketMessageType.MESSAGE_UPDATE,
                data = MessageStatusUpdate(
                    id = message.id,
                    received = true,
                    seen = false
                )
            ),
        )
    }

    fun updateMessagesSeen(
        dto: MessageUpdateDto
    ) {
        messageRepository.updateMessageSeen(dto.messageIds)
        dto.messageIds.forEach { messageId ->
            val message = messageRepository.findById(messageId).get()
            val senderId = message.sender.id!!
            webSocketHandler.sendToUser(
                userId = senderId,
                data = WebSocketMessageDto(
                    type = WebSocketMessageType.MESSAGE_UPDATE,
                    data = MessageStatusUpdate(
                        id = message.id,
                        received = true,
                        seen = true
                    )
                ),
            )
        }
    }
}