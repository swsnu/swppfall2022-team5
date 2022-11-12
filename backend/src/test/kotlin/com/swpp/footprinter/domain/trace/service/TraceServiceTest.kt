package com.swpp.footprinter.domain.trace.service

import com.swpp.footprinter.common.utils.dateToString8601
import com.swpp.footprinter.domain.footprint.dto.FootprintRequest
import com.swpp.footprinter.domain.footprint.repository.FootprintRepository
import com.swpp.footprinter.domain.footprint.service.FootprintService
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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.util.*
import javax.transaction.Transactional

@SpringBootTest
class TraceServiceTest @Autowired constructor(
    private val testHelper: TestHelper,
    private val footprintRepo: FootprintRepository,
    private val placeRepo: PlaceRepository,
    private val tagRepo: TagRepository,
    private val userRepo: UserRepository,
    private val traceRepo: TraceRepository,
    private val photoRepo: PhotoRepository,
    private val traceService: TraceService,
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

    /**
     * Testing createTrace
     */
    @Transactional
    @Test
    fun `Can create trace`() {
        // given
        val currentUser = testHelper.createUser("testuser", "test@email.com")
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

        val mockFootprintService = mock<FootprintService>(FootprintService::class.java)

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
        assertThat(createdFootprint).extracting{it.memo}.isEqualTo("testmemo")

        // Check place
        val createdPlace = createdFootprint.place
        assertThat(createdPlace).extracting{it.name}.isEqualTo("testplace")
        assertThat(createdPlace).extracting{it.address}.isEqualTo("testaddr")
        assertThat(createdPlace.footprints).contains(createdFootprint)

        // Check tag
        val createdTag = createdFootprint.tag
        assertThat(createdTag).extracting{it.tagCode.ordinal}.isEqualTo(0)
        assertThat(createdTag.taggedFootprints).contains(createdFootprint)

        // Check photos
        assertThat(createdFootprint.photos).hasSize(1)
        val createdPhoto = createdFootprint.photos.first()
        assertThat(createdPhoto).extracting{it.imagePath}.isEqualTo("testpath")
        assertThat(createdPhoto).extracting{it.longitude}.isEqualTo(0.0)
        assertThat(createdPhoto).extracting{it.latitude}.isEqualTo(0.0)
        assertThat(createdPhoto).extracting{it.timestamp}.isEqualTo(current)
        assertThat(createdPhoto).extracting{it.footprint?.id}.isEqualTo(createdFootprint.id)
    }
}
