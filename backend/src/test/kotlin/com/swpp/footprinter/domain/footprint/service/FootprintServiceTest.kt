package com.swpp.footprinter.domain.footprint.service

import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.common.utils.ImageUrlUtil
import com.swpp.footprinter.common.utils.dateToString8601
import com.swpp.footprinter.domain.footprint.dto.FootprintRequest
import com.swpp.footprinter.domain.footprint.dto.FootprintResponse
import com.swpp.footprinter.domain.footprint.repository.FootprintRepository
import com.swpp.footprinter.domain.photo.dto.PhotoRequest
import com.swpp.footprinter.domain.photo.repository.PhotoRepository
import com.swpp.footprinter.domain.place.dto.PlaceRequest
import com.swpp.footprinter.domain.place.repository.PlaceRepository
import com.swpp.footprinter.domain.tag.TAG_CODE
import com.swpp.footprinter.domain.tag.repository.TagRepository
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.repository.UserRepository
import com.swpp.footprinter.global.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FootprintServiceTest @Autowired constructor(
    private val testHelper: TestHelper,
    @MockBean val mockImageUrlUtil: ImageUrlUtil,
    @InjectMocks private val footprintService: FootprintService,
    private val footprintRepo: FootprintRepository,
    private val placeRepo: PlaceRepository,
    private val tagRepo: TagRepository,
    private val userRepo: UserRepository,
    private val traceRepo: TraceRepository,
    private val photoRepo: PhotoRepository,
) {

    @BeforeAll
    fun initialSetup() {
        testHelper.createUser("testUser", "test@snu.ac.kr")
        testHelper.initializeTag()
        `when`(mockImageUrlUtil.getImageURLfromImagePath(anyString())).thenReturn("testURL")
    }

    @AfterEach
    fun cleanUp() {
        photoRepo.deleteAll()
        footprintRepo.deleteAll()
        traceRepo.deleteAll()
        placeRepo.deleteAll()
    }

    @Test
    fun `should get footprint by id`() {
        val user = userRepo.findByIdOrNull(1)!!
        val testFootprint = testHelper.createFootprintAndUpdateElements(
            startTime = Date(),
            endTime = Date(),
            rating = 2,
            trace = testHelper.createTrace("TestTitle", "TestDate", user),
            place = testHelper.createPlace("TestPlace", "TestAddress"),
            tag = tagRepo.findByTagCode(TAG_CODE.음식점)!!,
            memo = "TEST",
            photos = mutableSetOf(testHelper.createPhoto("TestPath", 0.0, 0.0, Date(), null))
        )

        val footprint = footprintService.getFootprintById(testFootprint.id)

        assertThat(footprint).isExactlyInstanceOf(FootprintResponse::class.java)
        assertThat(footprint).extracting("rating").isEqualTo(2)
        assertThat(footprint).extracting("memo").isEqualTo("TEST")
        assertThat(footprint).extracting { it.place.name }.isEqualTo("TestPlace")
        assertThat(footprint).extracting { it.tag.tagName }.isEqualTo("음식점")
    }

    @Test
    fun `should throw error when requesting non-existing footprint`() {
        try {
            footprintService.getFootprintById(123)
            assertThat(true).isFalse
        } catch (e: FootprinterException) {
            assertThat(e).extracting("errorType").isEqualTo(ErrorType.NOT_FOUND)
        }
    }

    @Test
    fun `should create footprint and return`() {
        val user = userRepo.findByIdOrNull(1)!!
        testHelper.createPhoto("TestPath", 0.0, 0.0, Date())

        val footprintRequest = FootprintRequest(
            startTime = dateToString8601(Date()),
            endTime = dateToString8601(Date()),
            memo = "TEST2",
            tagId = 3,
            place = PlaceRequest("TestPlace", "TestAddress"),
            rating = 1,
            photos = listOf(PhotoRequest("TestPath", 0.0, 0.0, dateToString8601(Date())))
        )

        assertThat(footprintRepo.count()).isEqualTo(0)

        val trace = testHelper.createTrace("TestTitle", "TestDate", user)
        val actualFootprint = footprintService.createFootprintAndReturn(footprintRequest, trace)

        assertThat(footprintRepo.count()).isEqualTo(1)

        assertThat(actualFootprint).extracting("rating").isEqualTo(1)
        assertThat(actualFootprint).extracting("memo").isEqualTo("TEST2")
        assertThat(actualFootprint).extracting { it.tag.tagCode.name }.isEqualTo("음식점")
        assertThat(actualFootprint).extracting { it.place.name }.isEqualTo("TestPlace")

        val anotherRequest = FootprintRequest(
            startTime = dateToString8601(Date()),
            endTime = dateToString8601(Date()),
            memo = "TEST3",
            tagId = 4,
            place = PlaceRequest("TestPlace", "TestAddress"),
            rating = 0,
            photos = listOf(PhotoRequest("TestPath", 0.0, 0.0, dateToString8601(Date())))
        )

        val anotherFootprint = footprintService.createFootprintAndReturn(anotherRequest, trace)

        assertThat(footprintRepo.count()).isEqualTo(2)

        assertThat(anotherFootprint).extracting("rating").isEqualTo(0)
        assertThat(anotherFootprint).extracting("memo").isEqualTo("TEST3")
        assertThat(anotherFootprint).extracting { it.tag.tagCode.name }.isEqualTo("카페")
        assertThat(anotherFootprint).extracting { it.place.name }.isEqualTo("TestPlace")
    }

    @Test
    fun `should throw error with wrong footprint request`() {
        val user = userRepo.findByIdOrNull(1)!!
        val wrongTagRequest = FootprintRequest(
            startTime = dateToString8601(Date()),
            endTime = dateToString8601(Date()),
            memo = "TEST3",
            tagId = 123,
            place = PlaceRequest("TestPlace2", "TestAddress2"),
            rating = 0,
            photos = listOf()
        )

        val trace = testHelper.createTrace("TestTitle", "TestDate", user)

        try {
            footprintService.createFootprintAndReturn(wrongTagRequest, trace)
            assertThat(true).isFalse
        } catch (e: FootprinterException) {
            assertThat(e).extracting("errorType").isEqualTo(ErrorType.WRONG_FORMAT)
        }

        val wrongPhotoRequest = FootprintRequest(
            startTime = dateToString8601(Date()),
            endTime = dateToString8601(Date()),
            memo = "TEST4",
            tagId = 3,
            place = PlaceRequest("TestPlace3", "TestAddress3"),
            rating = 0,
            photos = listOf(PhotoRequest("WrongPath", 0.0, 0.0, null))
        )

        try {
            footprintService.createFootprintAndReturn(wrongPhotoRequest, trace)
            assertThat(true).isFalse
        } catch (e: FootprinterException) {
            assertThat(e).extracting("errorType").isEqualTo(ErrorType.NOT_FOUND)
        }
    }

    @Test
    fun `should edit footprint`() {
        val user = userRepo.findByIdOrNull(1)!!
        val testFootprint = testHelper.createFootprintAndUpdateElements(
            startTime = Date(),
            endTime = Date(),
            rating = 2,
            trace = testHelper.createTrace("TestTitle", "TestDate", user),
            place = testHelper.createPlace("TestPlace", "TestAddress"),
            tag = tagRepo.findByTagCode(TAG_CODE.음식점)!!,
            memo = "TEST",
            photos = mutableSetOf(testHelper.createPhoto("TestPath", 0.0, 0.0, Date(), null))
        )

        testHelper.createPhoto(imagePath = "TestPath2", 0.0, 0.0, Date())

        val footprintRequest = FootprintRequest(
            startTime = dateToString8601(Date()),
            endTime = dateToString8601(Date()),
            memo = "TESTEDIT",
            tagId = 4,
            place = PlaceRequest("TestPlace", "TestAddress"),
            rating = 1,
            photos = listOf(PhotoRequest("TestPath2", 0.0, 0.0, dateToString8601(Date())))
        )

        val wrongFootprintRequest = FootprintRequest(
            startTime = dateToString8601(Date()),
            endTime = dateToString8601(Date()),
            memo = "TESTEDIT",
            tagId = 999,
            place = PlaceRequest("TestPlace", "TestAddress"),
            rating = 1,
            photos = listOf(PhotoRequest("TestPath", 0.0, 0.0, dateToString8601(Date())))
        )

        try {
            footprintService.editFootprint(user, 99, footprintRequest)
            assertThat(true).isFalse
        } catch (e: FootprinterException) {
            assertThat(e).extracting("errorType").isEqualTo(ErrorType.NOT_FOUND)
        }

        try {
            footprintService.editFootprint(user, testFootprint.id, wrongFootprintRequest)
            assertThat(true).isFalse
        } catch (e: FootprinterException) {
            assertThat(e).extracting("errorType").isEqualTo(ErrorType.WRONG_FORMAT)
        }

        footprintService.editFootprint(user, testFootprint.id, footprintRequest)

        val footprint = footprintRepo.findByIdOrNull(testFootprint.id)!!

        assertThat(footprint).extracting("memo").isEqualTo("TESTEDIT")
        assertThat(footprint).extracting("rating").isEqualTo(1)
        assertThat(footprint).extracting { it.tag.tagCode.name }.isEqualTo("카페")
        assertThat(footprint).extracting { it.place.name }.isEqualTo("TestPlace")
        assertThat(footprint).extracting { it.photos.last().imagePath }.isEqualTo(("TestPath2"))

        val anotherRequest = FootprintRequest(
            startTime = dateToString8601(Date()),
            endTime = dateToString8601(Date()),
            memo = "TESTEDIT2",
            tagId = 4,
            place = PlaceRequest("TestPlace2", "TestAddress2"),
            rating = 2,
            photos = listOf(PhotoRequest("TestPath2", 0.0, 0.0, dateToString8601(Date())))
        )

        footprintService.editFootprint(user, testFootprint.id, anotherRequest)

        val anotherFootprint = footprintRepo.findByIdOrNull(testFootprint.id)!!

        assertThat(anotherFootprint).extracting("memo").isEqualTo("TESTEDIT2")
        assertThat(anotherFootprint).extracting("rating").isEqualTo(2)
        assertThat(anotherFootprint).extracting { it.tag.tagCode.name }.isEqualTo("카페")
        assertThat(anotherFootprint).extracting { it.place.name }.isEqualTo("TestPlace2")
        assertThat(anotherFootprint).extracting { it.photos.last().imagePath }.isEqualTo(("TestPath2"))
    }

    @Test
    fun `should delete footprint by id`() {
        val user = userRepo.findByIdOrNull(1)!!
        val testFootprint = testHelper.createFootprintAndUpdateElements(
            startTime = Date(),
            endTime = Date(),
            rating = 2,
            trace = testHelper.createTrace("TestTitle", "TestDate", user),
            place = testHelper.createPlace("TestPlace", "TestAddress"),
            tag = tagRepo.findByTagCode(TAG_CODE.음식점)!!,
            memo = "TEST",
            photos = mutableSetOf(testHelper.createPhoto("TestPath", 0.0, 0.0, Date(), null))
        )

        val footprint = footprintRepo.findByIdOrNull(testFootprint.id)
        assertThat(footprint).isNotNull

        footprintService.deleteFootprintById(user, testFootprint.id)

        val test = footprintRepo.findByIdOrNull(testFootprint.id)
        assertThat(test).isNull()
    }
}
