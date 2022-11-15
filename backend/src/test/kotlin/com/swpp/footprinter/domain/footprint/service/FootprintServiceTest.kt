package com.swpp.footprinter.domain.footprint.service

import com.swpp.footprinter.domain.footprint.dto.FootprintResponse
import com.swpp.footprinter.domain.footprint.model.Footprint
import com.swpp.footprinter.domain.footprint.repository.FootprintRepository
import com.swpp.footprinter.domain.place.model.Place
import com.swpp.footprinter.domain.place.repository.PlaceRepository
import com.swpp.footprinter.domain.tag.TAG_CODE
import com.swpp.footprinter.domain.tag.model.Tag
import com.swpp.footprinter.domain.tag.repository.TagRepository
import com.swpp.footprinter.domain.trace.model.Trace
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.model.User
import com.swpp.footprinter.domain.user.repository.UserRepository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
@Transactional
internal class FootprintServiceTest @Autowired constructor(
    private val footprintService: FootprintService,
    private val footprintRepo: FootprintRepository,
    private val placeRepo: PlaceRepository,
    private val tagRepo: TagRepository,
    private val userRepo: UserRepository,
    private val traceRepo: TraceRepository
) {

    @BeforeEach
    fun setup() {
        val user = userRepo.save(User(username = "User1", email = "test@snu.ac.kr", myTrace = mutableSetOf()))
        val place = placeRepo.save(Place("Test Place", "Test Address", mutableSetOf()))
        val tag = tagRepo.save(Tag(TAG_CODE.음식점, mutableSetOf()))
        val trace = traceRepo.save(Trace("Test Title", "Test Date", user, mutableSetOf()))
        footprintRepo.save(
            Footprint(
                startTime = Date(System.currentTimeMillis()),
                endTime = Date(System.currentTimeMillis()),
                rating = 2,
                memo = "TEST",
                place = place,
                tag = tag,
                trace = trace,
            )
        )
    }

    @Test
    fun `should get footprint by id`() {
        val footprint = footprintService.getFootprintById(1)

        assertThat(footprint).isExactlyInstanceOf(FootprintResponse::class.java)
        assertThat(footprint.id).isEqualTo(1)
        assertThat(footprint.rating).isEqualTo(2)
        assertThat(footprint.memo).isEqualTo("TEST")
        assertThat(footprint.place.name).isEqualTo("Test Place")
        assertThat(footprint.tag.tagName).isEqualTo("음식점")
        assertThat(footprint.traceId).isEqualTo(1)
    }

    @Test
    fun createFootprintAndReturn() {
    }

    @Test
    fun editFootprint() {
    }

    @Test
    fun deleteFootprintById() {
    }
}
