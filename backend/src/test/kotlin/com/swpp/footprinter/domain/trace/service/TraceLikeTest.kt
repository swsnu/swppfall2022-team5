package com.swpp.footprinter.domain.trace.service

import com.swpp.footprinter.domain.trace.repository.TraceLikeRepository
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.repository.UserRepository
import com.swpp.footprinter.global.TestHelper
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.assertj.core.api.Assertions.assertThat

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TraceLikeTest(
    @Autowired private val traceLikeService: TraceLikeService,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val traceRepository: TraceRepository,
    @Autowired private val traceLikeRepository: TraceLikeRepository,
    @Autowired private val testHelper: TestHelper
) {

    @BeforeAll
    fun initialSetup() {
        val user = testHelper.createUser("testuser", "test@email.com")
        testHelper.createUser("testuser2", "test@email.com")
        testHelper.createTrace(traceTitle = "test", traceDate = "test", owner = user, footprints = mutableSetOf())
    }

    @Test
    fun `test - like and unlike`() {
        val user = userRepository.findByUsername("testuser")!!
        val user2 = userRepository.findByUsername("testuser2")!!
        val trace = traceRepository.findByTraceTitle("test")!!
        var likesCount = traceLikeService.likeTraceById(user, traceId = trace.id)
        assertThat(likesCount).isEqualTo(1)
        likesCount = traceLikeService.likeTraceById(user, traceId = trace.id)
        likesCount = traceLikeService.likeTraceById(user2, traceId = trace.id)
        assertThat(likesCount).isEqualTo(2)
        assertThat(traceLikeRepository.findAllByTraceId(traceId = trace.id).count()).isEqualTo(2)
        likesCount = traceLikeService.unlikeTraceById(user, trace.id)
        assertThat(traceLikeRepository.findAllByTraceId(traceId = trace.id).count()).isEqualTo(1)
        assertThat(likesCount).isEqualTo(1)
        likesCount = traceLikeService.unlikeTraceById(user, trace.id)
        likesCount = traceLikeService.unlikeTraceById(user, trace.id)
        likesCount = traceLikeService.unlikeTraceById(user2, trace.id)
        assertThat(likesCount).isEqualTo(0)
        assertThat(traceLikeRepository.findAllByTraceId(traceId = trace.id).count()).isEqualTo(0)
    }
}
