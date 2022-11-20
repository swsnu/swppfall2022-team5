package com.swpp.footprinter.domain.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.swpp.footprinter.domain.auth.api.AuthController
import com.swpp.footprinter.domain.auth.dto.AuthResponse
import com.swpp.footprinter.domain.auth.dto.SignInRequest
import com.swpp.footprinter.domain.auth.dto.SignUpRequest
import com.swpp.footprinter.domain.user.dto.UserResponse
import com.swpp.footprinter.domain.user.repository.UserRepository
import io.kotest.matchers.types.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders.*

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthTest(
    @Autowired private val authController: AuthController,
    @Autowired private val userRepo: UserRepository
) {

    @Autowired
    private lateinit var mockMvc: MockMvc

    val objectMapper = ObjectMapper()

    @BeforeAll
    fun hello() {
        objectMapper.registerModule(KotlinModule())
    }

    @Test
    fun `test - signin`() {
        mockMvc.perform(
            post("/api/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(SignUpRequest(username = "footprinter", password = "footprintpw")))
        )
            .andExpect(status().isOk)

        userRepo.existsByUsername("footprinter")

        val signInResult = mockMvc.perform(
            post("/api/v1/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(SignInRequest(username = "footprinter", password = "footprintpw")))
        )
            .andExpect(status().isOk)
            .andReturn()

        val authResponse = objectMapper.readValue(signInResult.response.contentAsString, AuthResponse::class.java)
        val accessToken = authResponse.accessToken

        val meResult = mockMvc.perform(
            get("/api/v1/me")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isOk)
            .andReturn()

        val userResponse = objectMapper.readValue(meResult.response.contentAsString, UserResponse::class.java)
        assertThat(userResponse.username).isEqualTo("footprinter")
    }
}
