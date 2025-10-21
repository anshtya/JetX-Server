package com.anshtya.jetx.server.messaging.controller

import com.anshtya.jetx.server.messaging.dto.CreateGroupDto
import com.anshtya.jetx.server.messaging.dto.GetGroupDto
import com.anshtya.jetx.server.messaging.entity.Group
import com.anshtya.jetx.server.messaging.service.GroupService
import com.anshtya.jetx.server.security.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/group")
class GroupController(
    private val jwtUtil: JwtUtil,
    private val groupService: GroupService
) {
    @PostMapping("/create")
    fun createGroup(
        @RequestBody createGroupDto: CreateGroupDto
    ): ResponseEntity<Unit> {
        groupService.createGroup(createGroupDto)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/get")
    fun getGroup(
        @RequestBody getGroupDto: GetGroupDto
    ): ResponseEntity<Group> {
        val group = groupService.getGroup(getGroupDto.id)
        return ResponseEntity.ok(group)
    }

    @GetMapping("/list/get")
    fun getUserGroups(
        request: HttpServletRequest
    ): ResponseEntity<List<Group>> {
        val userId = jwtUtil.getUserIdFromRequest(request)
        val groups = groupService.getUserGroups(userId)
        return ResponseEntity.ok(groups)
    }
}