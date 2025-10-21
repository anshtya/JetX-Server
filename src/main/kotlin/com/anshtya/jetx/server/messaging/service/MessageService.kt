package com.anshtya.jetx.server.messaging.service

import com.anshtya.jetx.server.messaging.dto.SaveMessageDto
import com.anshtya.jetx.server.messaging.entity.Message
import com.anshtya.jetx.server.messaging.entity.MessageType
import com.anshtya.jetx.server.messaging.repository.MessageRepository
import com.anshtya.jetx.server.userprofile.service.UserProfileService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class MessageService(
    private val messageRepository: MessageRepository,
    private val groupService: GroupService,
    private val userProfileService: UserProfileService
) {
    fun saveMessage(
        saveMessageDto: SaveMessageDto
    ) {
        val sender = userProfileService.getUserProfile(saveMessageDto.senderId)

        val message = when (saveMessageDto.type) {
            MessageType.INDIVIDUAL -> Message.createIndividualMessage(
                sender = sender,
                receiver = userProfileService.getUserProfile(saveMessageDto.targetId),
                content = saveMessageDto.content
            )
            MessageType.GROUP -> Message.createGroupMessage(
                sender = sender,
                group = groupService.getGroup(saveMessageDto.targetId),
                content = saveMessageDto.content
            )
        }
        val savedMessage = messageRepository.save(message)
    }
}