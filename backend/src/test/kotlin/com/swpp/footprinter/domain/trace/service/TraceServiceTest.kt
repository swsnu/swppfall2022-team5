package com.swpp.footprinter.domain.trace.service

import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.common.utils.ImageUrlUtil
import com.swpp.footprinter.common.utils.dateToString8601
import com.swpp.footprinter.common.utils.dateToStringWithoutTime
import com.swpp.footprinter.common.utils.stringToDate8601
import com.swpp.footprinter.domain.footprint.dto.FootprintInitialTraceResponse
import com.swpp.footprinter.domain.footprint.dto.FootprintRequest
import com.swpp.footprinter.domain.footprint.repository.FootprintRepository
import com.swpp.footprinter.domain.photo.dto.PhotoInitialTraceResponse
import com.swpp.footprinter.domain.photo.dto.PhotoRequest
import com.swpp.footprinter.domain.photo.repository.PhotoRepository
import com.swpp.footprinter.domain.place.dto.PlaceInitialTraceResponse
import com.swpp.footprinter.domain.place.dto.PlaceRequest
import com.swpp.footprinter.domain.place.repository.PlaceRepository
import com.swpp.footprinter.common.externalAPI.KakaoAPIService
import com.swpp.footprinter.domain.tag.TAG_CODE
import com.swpp.footprinter.domain.tag.dto.TagResponse
import com.swpp.footprinter.domain.tag.repository.TagRepository
import com.swpp.footprinter.domain.trace.dto.TraceRequest
import com.swpp.footprinter.domain.trace.dto.TraceSearchRequest
import com.swpp.footprinter.domain.trace.dto.TraceViewResponse
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.dto.UserRequest
import com.swpp.footprinter.domain.user.repository.UserRepository
import com.swpp.footprinter.global.TestHelper
import io.kotest.common.runBlocking
import kotlinx.coroutines.delay
import net.minidev.json.JSONObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    private val traceService: TraceServiceImpl,
    @MockBean val mockImageUrlUtil: ImageUrlUtil,
    @MockBean val mockKakaoApiService: KakaoAPIService,
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
//            dateToString8601(current),
            footprintList = listOf(footprintRequest),
        )
        assertThat(traceRepo.count()).isEqualTo(0)

        // when
        traceService.createTrace(traceRequest, currentUser)

        // then
        // Check trace
        assertThat(traceRepo.count()).isEqualTo(1)
        val createdTrace = traceRepo.findAll().first()
        assertThat(createdTrace).extracting("traceTitle").isEqualTo("titleTrace")
        assertThat(createdTrace).extracting("traceDate").isEqualTo(dateToStringWithoutTime(current))
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

    @Test
    @Transactional
    fun `Could add only footprints when trace already exists`() {
        // given
        val currentUser = userRepo.findByIdOrNull(1)!!
        val current = Date()

        testHelper.createPhoto("testpath1", 0.0, 0.0, current)
        val photoRequest1 = PhotoRequest(
            imagePath = "testpath1",
            longitude = 0.0,
            latitude = 0.0,
            timestamp = dateToString8601(current)
        )
        testHelper.createPhoto("testpath2", 10.0, 10.0, current)
        val photoRequest2 = PhotoRequest(
            imagePath = "testpath2",
            longitude = 10.0,
            latitude = 10.0,
            timestamp = dateToString8601(current)
        )
        testHelper.createPhoto("testpath3", 20.0, 20.0, current)
        val photoRequest3 = PhotoRequest(
            imagePath = "testpath3",
            longitude = 20.0,
            latitude = 20.0,
            timestamp = dateToString8601(current)
        )

        val placeRequest1 = PlaceRequest(
            name = "testplace1",
            address = "testaddr1"
        )
        val placeRequest2 = PlaceRequest(
            name = "testplace2",
            address = "testaddr2"
        )
        val placeRequest3 = PlaceRequest(
            name = "testplace3",
            address = "testaddr3"
        )

        val footprintRequest1 = FootprintRequest(
            startTime = dateToString8601(current),
            endTime = dateToString8601(current),
            rating = 1,
            memo = "testmemo",
            tagId = 0,
            photos = listOf(photoRequest1),
            place = placeRequest1,
        )
        val footprintRequest2 = FootprintRequest(
            startTime = dateToString8601(current),
            endTime = dateToString8601(current),
            rating = 1,
            memo = "testmemo",
            tagId = 1,
            photos = listOf(photoRequest2),
            place = placeRequest2,
        )
        val footprintRequest3 = FootprintRequest(
            startTime = dateToString8601(current),
            endTime = dateToString8601(current),
            rating = 1,
            memo = "testmemo",
            tagId = 2,
            photos = listOf(photoRequest3),
            place = placeRequest3,
        )

        val traceRequest = TraceRequest(
            "titleTrace",
//            dateToString8601(current),
            footprintList = listOf(footprintRequest1, footprintRequest2, footprintRequest3),
        )

        val trace = testHelper.createTrace(
            traceTitle = "titleTrace",
            traceDate = dateToStringWithoutTime(current),
            owner = currentUser,
        )

        // when
        traceService.createTrace(traceRequest, currentUser)

        // then
        assertEquals(traceRepo.count(), 1)
        assertEquals(traceRepo.getReferenceById(trace.id).footprints.size, 3)
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
        val searchedTrace = traceService.getTraceById(trace.id)

        // then
        // Trace
        assertThat(searchedTrace.id).isEqualTo(trace.id)
        assertThat(searchedTrace.date).isEqualTo(trace.traceDate)
        assertThat(searchedTrace.title).isEqualTo(trace.traceTitle)
        assertThat(searchedTrace.owner).isEqualTo(trace.owner.toResponse(mockImageUrlUtil))
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
        )

        // when
        traceService.deleteTraceById(trace.id!!, user)

        // then
        assertThat(traceRepo.findByIdOrNull(trace.id)).isNull()
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
        val searchedTrace = traceService.getTraceByDate(dateToString8601(date), user)

        // then
        // Trace
        assertEquals(trace.id, searchedTrace!!.id)
        assertThat(searchedTrace.date).isEqualTo(trace.traceDate)
        assertThat(searchedTrace.title).isEqualTo(trace.traceTitle)
        assertThat(searchedTrace.owner).isEqualTo(trace.owner.toResponse(mockImageUrlUtil))
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

    /**
     * Test isNearEnough
     */
    @Transactional
    @Test
    fun `Could return photo location is near enough to given coordinate`() {
        // given
        val current = Date()
        val (longitude, latitude) = Pair(126.9490999, 37.4590445)
        val photoInitialTraceResponseNear = PhotoInitialTraceResponse(
            1, "testpath", "testurl", 126.9490947, 37.4590318, current
        )
        val photoInitialTraceResponseFar = PhotoInitialTraceResponse(
            1, "testpath", "testurl", 126.9505863, 37.4592141, current
        )

        // when
        val nearResult = traceService.isNearEnough(photoInitialTraceResponseNear, latitude, longitude)
        val farResult = traceService.isNearEnough(photoInitialTraceResponseFar, latitude, longitude)

        // then
        assertTrue(nearResult)
        assertFalse(farResult)
    }

    /**
     * Test isSimilarTime
     */
    @Transactional
    @Test
    fun `Could return whether time is similar between photo's and given`() {
        // given
        val current = Date()
        val cal = Calendar.getInstance()

        cal.time = current
        cal.add(Calendar.MINUTE, 30)
        val currentNear = cal.time

        cal.time = current
        cal.add(Calendar.HOUR, 3)
        val currentFar = cal.time

        val photoInitialTraceResponseNear = PhotoInitialTraceResponse(
            1, "testpath", "testurl", 126.9490947, 37.4590318, currentNear
        )
        val photoInitialTraceResponseFar = PhotoInitialTraceResponse(
            2, "testpath", "testurl", 126.9490947, 37.4590318, currentFar
        )

        // when
        val nearTime = traceService.isSimilarTime(photoInitialTraceResponseNear, current)
        val farTime = traceService.isSimilarTime(photoInitialTraceResponseFar, current)

        // then
        assertTrue(nearTime)
        assertFalse(farTime)
    }

    /**
     * Test createInitialTraceBasedOnPhotoIdListGiven
     * ( and groupPhotosWithLocationAndTimeAndReturnInitialTraceDTOList)
     * ( and addRecomendedPlaceToInitialDTOList)
     */
    @Test
    @Transactional
    fun `Could create initial trace response based on photo id list given`() {
        // given
        val current = Date()

        val coordCreater = { i: Int ->
            (i * 0.000000001) + (0.1 * (i / 2))
        }
        val timeCreator = { i: Int ->
            val cal = Calendar.getInstance()
            cal.time = current
            cal.add(Calendar.MINUTE, i)
            cal.add(Calendar.HOUR, i / 2)
            cal.time
        }

        // Create 6 Photo Enitities
        val photos = (0 until 6).map { i ->
            testHelper.createPhoto(
                imagePath = "path$i",
                longitude = coordCreater(i),
                latitude = coordCreater(i),
                timestamp = timeCreator(i),
            )
        }

        // Mock KakaoApiService
        val responseMock = ResponseEntity(
            JSONObject.toJSONString(
                mapOf(
                    "documents" to listOf(
                        mapOf(
                            "place_name" to "place1",
                            "address_name" to "addr1",
                            "distance" to "1"
                        ),
                        mapOf(
                            "place_name" to "place2",
                            "address_name" to "addr2",
                            "distance" to "2"
                        ),
                    )
                )
            ),
            HttpStatus.OK
        )
        `when`(mockKakaoApiService.coordToPlace(anyString(), anyString(), anyString(), anyInt()))
            .thenReturn(
                responseMock
            )
        `when`(
            mockKakaoApiService
                .getDocumentsMapListFromResponse(
                    responseMock
                )
        ).thenCallRealMethod()

        // Mock getImageURLfromImagePath
        `when`(mockImageUrlUtil.getImageURLfromImagePath(anyString())).thenReturn("testurl")

        // when
        val initialDTOListReturned = traceService.createInitialTraceBasedOnPhotoIdListGiven(
            photos.map { it.imagePath }
        )

        // then
        // Expected value
        val initialDTOListExpected = (0 until 3).map { i ->
            FootprintInitialTraceResponse(
                photoList = ((i * 2)..(i * 2 + 1)).map { j ->
                    photos[j].run {
                        PhotoInitialTraceResponse(
                            id,
                            imagePath,
                            imageUrl = "testurl",
                            longitude,
                            latitude,
                            timestamp
                        )
                    }
                }.toMutableList(),
                meanLatitude = (coordCreater(i * 2 + 1) + coordCreater(i * 2)) / 2,
                meanLongitude = (coordCreater(i * 2 + 1) + coordCreater(i * 2)) / 2,
                meanTime = Date((photos[i * 2 + 1].timestamp.time + photos[i * 2].timestamp.time) / 2),
                startTime = photos[i * 2].timestamp,
                endTime = photos[i * 2 + 1].timestamp,
                recommendedPlaceList = listOf(
                    TAG_CODE.음식점, TAG_CODE.관광명소, TAG_CODE.문화시설, TAG_CODE.카페, TAG_CODE.숙박
                ).map { tagCode ->
                    PlaceInitialTraceResponse(
                        name = "place1",
                        address = "addr1",
                        distance = 1,
                        category = TagResponse(tagCode.ordinal, tagCode.name)
                    )
                }.toMutableList().also {
                    it.addAll(
                        listOf(TAG_CODE.음식점, TAG_CODE.관광명소, TAG_CODE.문화시설, TAG_CODE.카페, TAG_CODE.숙박).map { tagCode ->
                            PlaceInitialTraceResponse(
                                name = "place2",
                                address = "addr2",
                                distance = 2,
                                category = TagResponse(tagCode.ordinal, tagCode.name)
                            )
                        }
                    )
                },
            )
        }

        // assertion
        assertThat(initialDTOListReturned).isEqualTo(initialDTOListExpected)
    }

    @Test
    @Transactional
    fun `Throw NOT_FOUND when photo with given path doesn't exist while creating initial trace based on photo path given`() {
        // given
        val current = Date()

        // Create 6 Photo Enitities
        val photos = (0 until 1).map { i ->
            testHelper.createPhoto(
                imagePath = "path$i",
                longitude = 0.0,
                latitude = 0.0,
                timestamp = current,
            )
        }

        // when // then
        val exception = assertThrows<FootprinterException> {
            traceService.createInitialTraceBasedOnPhotoIdListGiven(listOf("null"))
        }
        assertEquals(exception.errorType, ErrorType.NOT_FOUND)
    }

    @Test
    @Transactional
    fun `should get all user's traces`() {
        val date = Date()
        val user = userRepo.findByIdOrNull(1)!!
        val user2 = testHelper.createUser(
            username = "tester",
            password = "testpw",
            myTrace = mutableSetOf()
        )

        val trace = testHelper.createTrace(
            "testMyTraceTitle",
            dateToString8601(date),
            user,
        )

        val footprint = testHelper.createFootprintAndUpdateElements(
            startTime = date,
            endTime = date,
            rating = 0,
            memo = "testMemo",
            place = testHelper.createPlace("testPlace", "testAddress"),
            tag = testHelper.createTag(TAG_CODE.음식점),
            trace = trace,
            photos = mutableSetOf(
                testHelper.createPhoto("testPath", 0.0, 0.0, date)
            )
        )

        runBlocking { delay(1000) }
        val dateLater = Date()
        val traceLater = testHelper.createTrace(
            "testAnotherTraceTitle",
            dateToString8601(dateLater),
            user,
        )

        val footprintLater = testHelper.createFootprintAndUpdateElements(
            startTime = dateLater,
            endTime = dateLater,
            rating = 2,
            memo = "testAnotherMemo",
            place = testHelper.createPlace("testAnotherPlace", "testAnotherAddress"),
            tag = testHelper.createTag(TAG_CODE.카페),
            trace = traceLater,
            photos = mutableSetOf(
                testHelper.createPhoto("testAnotherPath", 0.0, 0.0, dateLater)
            )
        )

        `when`(mockImageUrlUtil.getImageURLfromImagePath(anyString())).thenReturn("testurl")

        val myTraces = traceService.getAllUserTraces(user, user.username)
        assertThat(myTraces).hasSize(2)

        val firstTrace = if (myTraces[0].date!! < myTraces[1].date!!) myTraces[0] else myTraces[1]
        val lastTrace = if (myTraces[0].date!! < myTraces[1].date!!) myTraces[1] else myTraces[0]

        assertThat(firstTrace).extracting("date").isEqualTo(trace.traceDate)
        assertThat(firstTrace).extracting("title").isEqualTo(trace.traceTitle)

        assertThat(lastTrace).extracting("date").isEqualTo(traceLater.traceDate)
        assertThat(lastTrace).extracting("title").isEqualTo(traceLater.traceTitle)

        val otherUserTraces = traceService.getAllUserTraces(user2, user.username)
        assertThat(otherUserTraces).hasSize(2)
    }

    /**
     * Test getAllOtherUsersTraces
     */
    @Test
    @Transactional
    fun `Could get all other user's traces`() {
        // given
        val loginUser = testHelper.createUser(
            username = "login",
            password = "",
        )
        val otherUser = testHelper.createUser(
            username = "other",
            password = "",
        )

        val loginTrace = testHelper.createTrace(
            traceTitle = "loginTrace",
            traceDate = "2022-11-11",
            owner = loginUser,
        )
        val otherTrace = testHelper.createTrace(
            traceTitle = "otherTrace",
            traceDate = "2022-12-12",
            owner = otherUser,
        )
        val other2Trace = testHelper.createTrace(
            traceTitle = "other2Trace",
            traceDate = "2022-12-13",
            owner = otherUser,
            isPublic = false,
        )

        val expectedTraceDetailResponseList = listOf(
            otherTrace.toDetailResponse(mockImageUrlUtil)
        )

        // when
        val actualTraceDetailResponse = traceService.getAllOtherUsersTraces(loginUser)

        // then
        assertThat(actualTraceDetailResponse.size).isEqualTo(expectedTraceDetailResponseList.size)
        assertThat(actualTraceDetailResponse[0].id).isEqualTo(expectedTraceDetailResponseList[0].id)
    }

    /**
     * Test updateViewCount
     */
    @Test
    @Transactional
    fun `Could update view count`() {
        // given
        val user = testHelper.createUser(
            username = "",
            password = "",
        )
        val targetTrace = testHelper.createTrace(
            traceTitle = "",
            traceDate = "",
            owner = user,
        )
        val targetId = targetTrace.id
        val targetViewCount = targetTrace.viewCount

        val expectedTraceViewResponse = TraceViewResponse(targetViewCount + 1)

        // when
        val actualTraceViewResponse = traceService.updateViewCount(targetId)

        // then
        assertThat(actualTraceViewResponse).isEqualTo(expectedTraceViewResponse)
    }

    @Test
    @Transactional
    fun `Throw NOT_FOUND when target trace is not exists`() {
        // given //when //then
        val exception = assertThrows<FootprinterException> {
            traceService.updateViewCount(111)
        }
        assertEquals(exception.errorType, ErrorType.NOT_FOUND)
    }

    /**
     * Test searchTrace
     */
    @Test
    @Transactional
    fun `Could search trace with right options`() {
        // given
        val currentDate = Date()

        val users = (1..4).map {
            testHelper.createUser("user$it", "")
        }

        val traces = (1..8).map {
            testHelper.createTrace(
                traceTitle = "",
                traceDate = "2022-12-0${(it % 2 + 1)}",
                owner = users[(it - 1) / 2],
            )
        }

        val places = (1..2).map {
            testHelper.createPlace(
                name = "place${it % 2}",
                address = "placeaddr${it % 2}"
            )
        }

        val footprints = (1..8).map {
            testHelper.createFootprintAndUpdateElements(
                startTime = currentDate,
                endTime = currentDate,
                rating = 1,
                trace = traces[it - 1],
                tag = tagRepo.findByTagCode(TAG_CODE.values()[it % 2])!!,
                memo = "",
                place = places[it % 2],
            )
        }

        val traceSearchRequest = TraceSearchRequest(
            users = listOf(
                UserRequest(username = "user2"),
                UserRequest(username = "user5")
            ),
            dates = listOf(
                "2022-12-01"
            ),
            places = listOf(
                PlaceRequest(name = "place1", address = "placeaddr1"),
                PlaceRequest(name = "place0", address = "placeaddr0"),
            ),
            tags = listOf(0),
        )

        val expectedTraceDetailResponseList = listOf(
            traces.get(3).toDetailResponse(mockImageUrlUtil)
        )

        // when
        val actualTraceDetailResponseList = traceService.searchTrace(traceSearchRequest)

        // then
        assertThat(actualTraceDetailResponseList).isEqualTo(expectedTraceDetailResponseList)
    }
}
