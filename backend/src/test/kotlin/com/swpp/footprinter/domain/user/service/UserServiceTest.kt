package com.swpp.footprinter.domain.user.service

import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.common.utils.ImageUrlUtil
import com.swpp.footprinter.domain.footprint.repository.FootprintRepository
import com.swpp.footprinter.domain.photo.repository.PhotoRepository
import com.swpp.footprinter.domain.place.repository.PlaceRepository
import com.swpp.footprinter.domain.tag.repository.TagRepository
import com.swpp.footprinter.domain.trace.dto.TraceDetailResponse
import com.swpp.footprinter.domain.trace.dto.TraceResponse
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.dto.UserResponse
import com.swpp.footprinter.domain.user.repository.UserRepository
import com.swpp.footprinter.global.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import javax.transaction.Transactional

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    private val testHelper: TestHelper,
    private val userRepo: UserRepository,
    private val traceRepo: TraceRepository,
    private val footprintRepo: FootprintRepository,
    private val photoRepo: PhotoRepository,
    private val placeRepo: PlaceRepository,
    private val tagRepo: TagRepository,
    @MockBean val mockImageUrlUtil: ImageUrlUtil,
) {
    @BeforeEach
    fun setup() {
        testHelper.initializeTag()
    }

    @AfterEach
    fun cleanUp() {
        userRepo.findAll().forEach { it.myTrace.clear() }
        traceRepo.deleteAll()
        footprintRepo.deleteAll()
        photoRepo.deleteAll()
        placeRepo.deleteAll()
        tagRepo.deleteAll()
    }

    /**
     * Test getUserTraces
     */
    @Test
    @Transactional
    fun `Can get user traces by given user id`() {
        // given
        val user = testHelper.createUser(
            username = "testname",
            password = "testpassword",
            myTrace = mutableSetOf(),
        )
        val trace = testHelper.createTrace(
            traceTitle = "testtitle",
            traceDate = "2011-11-11",
            owner = user,
            footprints = mutableSetOf(),
        )
        user.myTrace.add(trace)

        // when
        val traceResponseListReturned = userService.getUserTraces(user.id)

        // then
        val traceResponseListExpected = listOf(
            TraceResponse(
                id = trace.id,
                date = trace.traceDate,
                title = trace.traceTitle,
                owner = trace.owner.toResponse(mockImageUrlUtil),
                likesCount = 0,
            )
        )
        assertEquals(traceResponseListReturned, traceResponseListExpected)
    }

    @Test
    @Transactional
    fun `Throw NOT_FOUND when user given by id is not exist while getting user by id`() {
        // given //when //then
        val exception = assertThrows<FootprinterException> { userService.getUserTraces(9999) }
        assertEquals(exception.errorType, ErrorType.NOT_FOUND)
    }

    /**
     * Test getUserTraceByDate
     */
    @Test
    @Transactional
    fun `Could get user's trace by given id and date`() {
        // given
        val user = testHelper.createUser(
            username = "testname",
            password = "testpassword",
            myTrace = mutableSetOf(),
        )

        val trace1 = testHelper.createTrace(
            traceTitle = "testtitle1",
            traceDate = "2011-11-11",
            owner = user,
            footprints = mutableSetOf(),
        )
        user.myTrace.add(trace1)

        val trace2 = testHelper.createTrace(
            traceTitle = "testtitle2",
            traceDate = "2022-11-11",
            owner = user,
            footprints = mutableSetOf(),
        )
        user.myTrace.add(trace2)

        // when
        val traceDetailResponseReturned = userService.getUserTraceByDate(user.id, "2022-11-11")

        // then
        val traceDetailResponseExpected = TraceDetailResponse(
            id = trace2.id,
            date = trace2.traceDate,
            title = trace2.traceTitle,
            owner = trace2.owner.toResponse(mockImageUrlUtil),
            footprints = listOf(),
            viewCount = 0,
            isLiked = false,
            likesCount = 0,
            isPublic = true,
        )

        assertThat(traceDetailResponseReturned?.id).isEqualTo(traceDetailResponseExpected.id)
        assertThat(traceDetailResponseReturned?.date).isEqualTo(traceDetailResponseExpected.date)
        assertThat(traceDetailResponseReturned?.title).isEqualTo(traceDetailResponseExpected.title)
        assertThat(traceDetailResponseReturned?.owner).isEqualTo(traceDetailResponseExpected.owner)
        assertThat(traceDetailResponseReturned?.footprints).isEmpty()
        assertThat(traceDetailResponseReturned?.viewCount).isEqualTo(traceDetailResponseExpected.viewCount)
    }

    @Test
    @Transactional
    fun `Throw NOT_FOUND when user given by id is not exist while getting user by id and date`() {
        // given // when // then
        val exception = assertThrows<FootprinterException> { userService.getUserTraceByDate(9999, "") }
        assertEquals(exception.errorType, ErrorType.NOT_FOUND)
    }

    /**
     * Test getUserByUserName
     */
    @Test
    @Transactional
    fun `Could get user by username`() {
        // given
        val user = testHelper.createUser(
            username = "testname",
            password = "testpassword",
            followerCount = 5,
            followingCount = 10,
        )
        val expectedUserResponse = UserResponse(
            username = "testname",
            followingCount = 10,
            followerCount = 5,
            traceCount = 0,
        )

        // when
        val actualUserResponse = userService.getUserByUsername("testname")

        // then
        assertThat(actualUserResponse).isEqualTo(expectedUserResponse)
    }

    @Test
    @Transactional
    fun `Throw NOT_FOUND when user doesn't exists with given username`() {
        // given
        val user = testHelper.createUser(
            username = "testname",
            password = "testpassword",
            followerCount = 5,
            followingCount = 10,
        )

        // when // then
        val exception = assertThrows<FootprinterException> { userService.getUserByUsername("nonExistName") }
        assertEquals(exception.errorType, ErrorType.NOT_FOUND)
    }
}
