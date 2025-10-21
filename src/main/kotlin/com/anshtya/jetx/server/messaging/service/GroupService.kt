package com.anshtya.jetx.server.messaging.service

import com.anshtya.jetx.server.messaging.dto.CreateGroupDto
import com.anshtya.jetx.server.messaging.entity.Group
import com.anshtya.jetx.server.messaging.repository.GroupRepository
import com.anshtya.jetx.server.userprofile.repository.UserProfileRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
@Transactional
class GroupService(
    private val groupRepository: GroupRepository,
    private val userProfileRepository: UserProfileRepository
) {
    fun createGroup(
        createGroupDto: CreateGroupDto
    ) {
        groupRepository.save(
            Group(
                name = createGroupDto.name,
                description = createGroupDto.description
            )
        )
    }

    fun getUserGroups(userId: UUID): List<Group> {
        return userProfileRepository.findGroupsByUserProfileId(userId)
    }

    fun getGroup(id: UUID): Group {
        return groupRepository.findById(id).getOrElse {
            throw IllegalStateException("Group doesn't exist")
        }
    }
}