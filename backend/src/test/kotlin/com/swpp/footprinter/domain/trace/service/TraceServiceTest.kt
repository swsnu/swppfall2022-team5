package com.swpp.footprinter.domain.trace.service

import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.common.utils.ImageUrlUtil
import com.swpp.footprinter.common.utils.dateToString8601
import com.swpp.footprinter.common.utils.stringToDate8601
import com.swpp.footprinter.domain.footprint.dto.FootprintRequest
import com.swpp.footprinter.domain.footprint.repository.FootprintRepository
import com.swpp.footprinter.domain.photo.dto.PhotoRequest
import com.swpp.footprinter.domain.photo.repository.PhotoRepository
import com.swpp.footprinter.domain.place.dto.PlaceRequest
import com.swpp.footprinter.domain.place.repository.PlaceRepository
import com.swpp.footprinter.domain.tag.TAG_CODE
import com.swpp.footprinter.domain.tag.repository.TagRepository
import com.swpp.footprinter.domain.trace.dto.TraceRequest
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.repository.UserRepository
import com.swpp.footprinter.global.TestHelper
import io.kotest.common.runBlocking
import kotlinx.coroutines.delay
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.repository.findByIdOrNull
import java.util.*
import javax.transaction.Transactional

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TraceServiceTest @Autowired constructor(
    private val testHelper: TestHelper,
    private val footprintRepo: FootprintRepository,
    private val placeRepo: PlaceRepository,
    private val tagRepo: TagRepository,
    private val userRepo: UserRepository,
    private val traceRepo: TraceRepository,
    private val photoRepo: PhotoRepository,
    private val traceService: TraceService,
    @MockBean val mockImageUrlUtil: ImageUrlUtil,
) {
    // TODO: After implementing user authentication, should cleanup user entities too.
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
    }

    @BeforeAll
    fun initialSetup() {
        testHelper.createUser("testuser", "test@email.com")
    }

    /**
     * Testing createTrace
     */
    @Transactional
    @Test
    fun `Can create trace`() {
        // given
        val currentUser = userRepo.findByIdOrNull(1)!!
        val current = Date()

        testHelper.createPhoto("testpath", 0.0, 0.0, current)

        val photoRequest = PhotoRequest(
            imagePath = "testpath",
            longitude = 0.0,
            latitude = 0.0,
            timestamp = dateToString8601(current)
        )
        val placeRequest = PlaceRequest(
            name = "testplace",
            address = "testaddr"
        )
        val footprintRequest = FootprintRequest(
            startTime = dateToString8601(current),
            endTime = dateToString8601(current),
            rating = 1,
            memo = "testmemo",
            tagId = 0,
            photos = listOf(photoRequest),
            place = placeRequest,
        )

        // TODO: Change to current user after user authentication is implemented
        val traceRequest = TraceRequest(
            "titleTrace",
            dateToString8601(current),
            footprintList = listOf(footprintRequest),
        )
        assertThat(traceRepo.count()).isEqualTo(0)

        // when
        traceService.createTrace(traceRequest)

        // then
        // Check trace
        assertThat(traceRepo.count()).isEqualTo(1)
        val createdTrace = traceRepo.findByIdOrNull(1)!!
        assertThat(createdTrace).extracting("traceTitle").isEqualTo("titleTrace")
        assertThat(createdTrace).extracting("traceDate").isEqualTo(dateToString8601(current))
        // TODO: After implementing user authentication, check whether current user
        assertThat(createdTrace).extracting { it.owner.id }.isEqualTo(currentUser.id)

        // Check footprints
        assertThat(createdTrace.footprints).hasSize(1)
        val createdFootprint = createdTrace.footprints.first()
        assertThat(createdFootprint).extracting("startTime").isEqualTo(current)
        assertThat(createdFootprint).extracting("endTime").isEqualTo(current)
        assertThat(createdFootprint).extracting("rating").isEqualTo(1)
        assertThat(createdFootprint).extracting { it.memo }.isEqualTo("testmemo")

        // Check place
        val createdPlace = createdFootprint.place
        assertThat(createdPlace).extracting { it.name }.isEqualTo("testplace")
        assertThat(createdPlace).extracting { it.address }.isEqualTo("testaddr")
        assertThat(createdPlace.footprints).contains(createdFootprint)

        // Check tag
        val createdTag = createdFootprint.tag
        assertThat(createdTag).extracting { it.tagCode.ordinal }.isEqualTo(0)
        assertThat(createdTag.taggedFootprints).contains(createdFootprint)

        // Check photos
        assertThat(createdFootprint.photos).hasSize(1)
        val createdPhoto = createdFootprint.photos.first()
        assertThat(createdPhoto).extracting { it.imagePath }.isEqualTo("testpath")
        assertThat(createdPhoto).extracting { it.longitude }.isEqualTo(0.0)
        assertThat(createdPhoto).extracting { it.latitude }.isEqualTo(0.0)
        assertThat(createdPhoto).extracting { it.timestamp }.isEqualTo(current)
        assertThat(createdPhoto).extracting { it.footprint?.id }.isEqualTo(createdFootprint.id)
    }

    /**
     * Testing getTraceById
     */
    @Test
    @Transactional
    fun `Could get trace by Id`() {
        // given
        val date = Date()
        val user = userRepo.findByIdOrNull(1)!!
        val trace = testHelper.createTrace(
            "testTraceTitle",
            dateToString8601(date),
            user,
        )
        val footprint = testHelper.createFootprintAndUpdateElements(
            startTime = date,
            endTime = date,
            rating = 1,
            memo = "testmemo",
            place = testHelper.createPlace("testplace", "testaddr"),
            tag = testHelper.createTag(TAG_CODE.음식점),
            trace = trace,
            photos = mutableSetOf(
                testHelper.createPhoto("testpath", 0.0, 0.0, date)
            )
        )

        `when`(mockImageUrlUtil.getImageURLfromImagePath(anyString())).thenReturn("testurl")

        // when
        val searchedTrace = traceService.getTraceById(trace.id!!)

        // then
        // Trace
        assertThat(searchedTrace.id).isEqualTo(trace.id)
        assertThat(searchedTrace.date).isEqualTo(trace.traceDate)
        assertThat(searchedTrace.title).isEqualTo(trace.traceTitle)
        assertThat(searchedTrace.ownerId).isEqualTo(trace.owner.id)
        assertThat(searchedTrace.footprints).hasSize(1)
        // Footprint
        assertThat(searchedTrace.footprints?.first()?.id).isEqualTo(footprint.id)
        val photoList = searchedTrace.footprints?.first()?.photos
        // Photo (transfered to url)
        assertThat(photoList).hasSize(1)
        assertThat(photoList?.first()?.imageUrl).isEqualTo("testurl")
    }

    @Test
    @Transactional
    fun `Throw NOT_FOUND when there is no trace for given id while get trace by id`() {
        val exception = assertThrows<FootprinterException> { traceService.getTraceById(1) }
        assertEquals(exception.errorType, ErrorType.NOT_FOUND)
    }

    /**
     * Test deleteTraceById
     */
    @Test
    @Transactional
    fun `Could delete trace by id`() {
        // given
        val date = Date()
        val user = userRepo.findByIdOrNull(1)!!
        val trace = testHelper.createTrace(
            "testTraceTitle",
            dateToString8601(date),
            user,
            mutableSetOf()
        )

        // when
        traceService.deleteTraceById(trace.id!!)

        // then
        assertThat(traceRepo.findByIdOrNull(trace.id!!)).isNull()
    }

    // TODO: add testcase about authentication failure after implemented user authentication

    /**
     * Testing getTraceByDate
     */
    @Test
    @Transactional
    fun `Could get trace by date`() {
        // given
        val date = Date()
        val user = userRepo.findByIdOrNull(1)!!
        val trace = testHelper.createTrace(
            "testTraceTitle",
            dateToString8601(date),
            user,
        )
        val footprint = testHelper.createFootprintAndUpdateElements(
            startTime = date,
            endTime = date,
            rating = 1,
            memo = "testmemo",
            place = testHelper.createPlace("testplace", "testaddr"),
            tag = testHelper.createTag(TAG_CODE.음식점),
            trace = trace,
            photos = mutableSetOf(
                testHelper.createPhoto("testpath", 0.0, 0.0, date)
            )
        )
        runBlocking { delay(1000) }
        val dateLater = Date()
        val footprintLater = testHelper.createFootprintAndUpdateElements(
            startTime = dateLater,
            endTime = dateLater,
            rating = 1,
            memo = "testmemo2",
            place = testHelper.createPlace("testplace2", "testaddr2"),
            tag = testHelper.createTag(TAG_CODE.음식점),
            trace = trace,
            photos = mutableSetOf(
                testHelper.createPhoto("testpath2", 0.0, 0.0, date)
            )
        )

        `when`(mockImageUrlUtil.getImageURLfromImagePath(anyString())).thenReturn("testurl")

        // when
        val searchedTrace = traceService.getTraceByDate(dateToString8601(date))

        // then
        // Trace
        assertEquals(trace.id, searchedTrace!!.id)
        assertThat(searchedTrace.date).isEqualTo(trace.traceDate)
        assertThat(searchedTrace.title).isEqualTo(trace.traceTitle)
        assertThat(searchedTrace.ownerId).isEqualTo(trace.owner.id)
        assertThat(searchedTrace.footprints).hasSize(2)
        // Footprint
        assertThat(searchedTrace.footprints?.first()?.id).isEqualTo(footprint.id)
        val photoList = searchedTrace.footprints?.first()?.photos
        // Photo (transfered to url)
        assertThat(photoList).hasSize(1)
        assertThat(photoList?.first()?.imageUrl).isEqualTo("testurl")
        // Check sorted
        assertThat(searchedTrace.footprints).isSortedAccordingTo { o1, o2 ->
            stringToDate8601(o1.startTime).compareTo(stringToDate8601(o2.startTime))
        }
    }
}
