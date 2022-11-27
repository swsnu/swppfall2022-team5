package com.swpp.footprinter.test

import com.swpp.footprinter.common.utils.dateToString8601
import com.swpp.footprinter.common.utils.stringToDate8601
import com.swpp.footprinter.domain.auth.dto.SignUpRequest
import com.swpp.footprinter.domain.auth.service.AuthService
import com.swpp.footprinter.domain.footprint.dto.FootprintRequest
import com.swpp.footprinter.domain.photo.dto.PhotoRequest
import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.photo.repository.PhotoRepository
import com.swpp.footprinter.domain.place.dto.PlaceRequest
import com.swpp.footprinter.domain.tag.TAG_CODE
import com.swpp.footprinter.domain.tag.model.Tag
import com.swpp.footprinter.domain.tag.repository.TagRepository
import com.swpp.footprinter.domain.trace.dto.TraceRequest
import com.swpp.footprinter.domain.trace.service.TraceService
import com.swpp.footprinter.domain.user.repository.UserRepository
import com.swpp.footprinter.domain.user.service.UserFollowService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
@Transactional
@Profile("!test")
class DataInitializer(
    private val authService: AuthService,
    private val followService: UserFollowService,
    private val photoRepo: PhotoRepository,
    private val tagRepo: TagRepository,
    private val traceService: TraceService,
    private val userRepo: UserRepository,
) : ApplicationRunner {

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {

        // 더미 사용자 11인 추가
        for (i in 1..11) {
            val name = "user$i"
            if (!userRepo.existsByUsername(name)) {
                authService.createUser(SignUpRequest(username = name, password = "1234"))
            }
        }
        // 1~5번은 서로 팔로우
        for (i in 1..5) {
            for (j in 1..5) {
                if (i == j) continue
                followService.followUser(userRepo.findByUsername("user$i")!!, "user$j")
            }
        }
        // 6~11번은 서로 팔로우
        for (i in 6..11) {
            for (j in 6..11) {
                if (i == j) continue
                followService.followUser(userRepo.findByUsername("user$i")!!, "user$j")
            }
        }
        // 6~10번은 1번을 팔로우
        for (j in 6..10) {
            followService.followUser(userRepo.findByUsername("user$j")!!, "user1")
        }
        // 1번은 11번을 팔로우
        followService.followUser(userRepo.findByUsername("user1")!!, "user11")

        for (tagCode in TAG_CODE.values()) {
            if (!tagRepo.existsByTagCode(tagCode)) {
                tagRepo.save(Tag(tagCode, mutableSetOf()))
            }
        }

        val photoList = mutableListOf<Photo>()
        // 사진 생성
        for (id in 1..11) {
            for (day in 22..26) {
                for (footprint in 1..5) {
                    val hour = String.format("%02d", (footprint * 3 + day * id) % 24)
                    for (photo in 1..3) {
                        val minute = String.format("%02d", (photo * 10 + day * footprint) % 60)
                        val newPhoto = Photo(
                            imagePath = "photo_${id}_${day}_${footprint}_$photo",
                            longitude = 127 + (id * day * footprint) % 300.toDouble() / 200,
                            latitude = 35.5 + (id * day * footprint) % 400.toDouble() / 200,
                            timestamp = stringToDate8601("2022-11-${day}T$hour:$minute:28.000+00:00"),
                            footprint = null,
                        )
                        photoList.add(newPhoto)
                        photoRepo.save(newPhoto)
                    }
                }
            }
        }

        // 유저별 trace 생성 : 22~26일, 하루에 footprint 5개, footprint당 사진 3개
        for (id in 1..11) {
            val user = userRepo.findByUsername("user$id")!!
            for (day in 22..26) {
                val footprintList = mutableListOf<FootprintRequest>()
                for (footprint in 1..5) {
                    val photos = photoList.subList((id - 1) * 75 + (day - 22) * 15 + (footprint - 1) * 3, (id - 1) * 75 + (day - 22) * 15 + (footprint) * 3)
                        .map {
                            PhotoRequest(
                                it.imagePath,
                                it.longitude,
                                it.latitude,
                                dateToString8601(it.timestamp),
                            )
                        }
                    footprintList.add(
                        FootprintRequest(
                            startTime = photos.first().timestamp,
                            endTime = photos.last().timestamp,
                            rating = (id * day * footprint) % 3,
                            memo = "${id * day * footprint}",
                            tagId = (id + day + footprint) % 5,
                            photos = photos,
                            place = PlaceRequest(
                                name = "장소 $id $day",
                                address = "서울특별시 관악구 관악로$id 서울대학교 ${day}동 ${footprint}호",
                            )
                        )
                    )
                }
                traceService.createTrace(
                    loginUser = user,
                    traceRequest = TraceRequest(
                        title = "${day}일의 trace",
                        date = "2022-11-$day",
                        public = day % 3 == 1,
                        footprintList = footprintList
                    )
                )
            }
        }

        println("initialize done!")
    }
}
