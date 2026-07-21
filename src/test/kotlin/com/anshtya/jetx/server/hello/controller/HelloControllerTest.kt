package com.anshtya.jetx.server.hello.controller

import com.anshtya.jetx.server.security.JwtAuthenticationFilter
import com.anshtya.jetx.server.security.JwtUtil
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(HelloController::class)
@AutoConfigureMockMvc(addFilters = false)
class HelloControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var jwtUtil: JwtUtil

    @MockitoBean
    private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    @Test
    fun `hello endpoint returns json response`() {
        mockMvc.perform(get("/hello"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("Server is running"))
    }
}
