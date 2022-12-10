package com.swpp.footprinter.domain.user.service

import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.domain.user.dto.UserFollowingResponse
import com.swpp.footprinter.domain.user.repository.UserFollowRepository
import com.swpp.footprinter.domain.user.repository.UserRepository
import com.swpp.footprinter.global.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import javax.persistence.EntityManager
import javax.transaction.Transactional

@SpringBootTest
class UserFollowServiceTest @Autowired constructor(
    private val userFollowService: UserFollowService,
    private val testHelper: TestHelper,
    private val userFollowRepo: UserFollowRepository,
    private val userRepo: UserRepository,
    private val entityManager: EntityManager,
) {
    @AfterEach
    fun cleanUp() {
        userFollowRepo.deleteAll()
        userRepo.deleteAll()
    }

    @Test
    @Transactional
    fun `Could follow and unfollow user`() {
        val user1 = testHelper.createUser(
            username = "BTS",
            password = "testpassword",
            myTrace = mutableSetOf(),
        )

        val user2 = testHelper.createUser(
            username = "tester",
            password = "testpassword",
            myTrace = mutableSetOf(),
        )

        val user3 = testHelper.createUser(
            username = "tester2",
            password = "testpassword",
            myTrace = mutableSetOf(),
        )

        userFollowService.followUser(user2, user1.username)
        userFollowService.followUser(user3, user1.username)
        try {
            userFollowService.followUser(user2, user1.username)
        } catch (_: DataIntegrityViolationException) {
        }
        entityManager.clear()

        val followerList = userFollowService.getUserFollowers(user1, user1.username)
        assertThat(followerList).hasSize(2)

        val followingList1 = userFollowService.getUserFollowings(user2, user2.username)
        assertThat(followingList1).hasSize(1)
        assertThat(followingList1).first().extracting("username").isEqualTo("BTS")

        val followingList2 = userFollowService.getUserFollowings(user3, user3.username)
        assertThat(followingList2).hasSize(1)
        assertThat(followingList2).first().extracting("username").isEqualTo("BTS")

        userFollowService.unfollowUser(user2, "BTS")
        try {
            userFollowService.unfollowUser(user2, "BTS")
        } catch (e: FootprinterException) {
            assertThat(e).extracting("errorType").isEqualTo(ErrorType.NOT_FOUND)
        }

        userFollowService.deleteFollower(user1, "tester2")
        try {
            userFollowService.deleteFollower(user1, "tester2")
        } catch (e: FootprinterException) {
            assertThat(e).extracting("errorType").isEqualTo(ErrorType.NOT_FOUND)
        }

        val followerListAfter = userFollowService.getUserFollowers(user1, user1.username)
        assertThat(followerListAfter).hasSize(0)
    }

    @Test
    @Transactional
    fun `Throw WRONG_FOLLOW_REQUEST when follow oneself`() {
        val user = testHelper.createUser(
            username = "user2",
            password = "testpassword",
            myTrace = mutableSetOf(),
        )

        try {
            userFollowService.followUser(user, "user2")
        } catch (e: FootprinterException) {
            assertThat(e).extracting("errorType").isEqualTo(ErrorType.WRONG_FOLLOW_REQUEST)
        }
    }

    @Test
    @Transactional
    fun `Could get follow info`() {
        for (i in 1..5) {
            val name = "user$i"
            testHelper.createUser(username = name, password = "testpassword")
        }

        val user1 = userRepo.findByUsername("user1")!!

        for (i in 2..5) {
            userFollowService.followUser(user1, "user$i")
        }

        val followCount = userFollowService.getUserFollowCount(user1, "user1")

        assertThat(followCount).extracting("followerCount").isEqualTo(0)
        assertThat(followCount).extracting("followingCount").isEqualTo(4)
    }

    @Test
    @Transactional
    fun `Throw NOT_FOUND when requesting about non-existing user`() {
        val user = testHelper.createUser(
            username = "user",
            password = "testpassword",
            myTrace = mutableSetOf(),
        )

        try {
            userFollowService.followUser(user, "non-existing")
        } catch (e: FootprinterException) {
            assertThat(e).extracting("errorType").isEqualTo(ErrorType.NOT_FOUND)
        }

        try {
            userFollowService.unfollowUser(user, "non-existing")
        } catch (e: FootprinterException) {
            assertThat(e).extracting("errorType").isEqualTo(ErrorType.NOT_FOUND)
        }

        try {
            userFollowService.unfollowUser(user, "non-existing")
        } catch (e: FootprinterException) {
            assertThat(e).extracting("errorType").isEqualTo(ErrorType.NOT_FOUND)
        }

        try {
            userFollowService.deleteFollower(user, "non-existing")
        } catch (e: FootprinterException) {
            assertThat(e).extracting("errorType").isEqualTo(ErrorType.NOT_FOUND)
        }

        try {
            userFollowService.getUserFollowers(user, "non-existing")
        } catch (e: FootprinterException) {
            assertThat(e).extracting("errorType").isEqualTo(ErrorType.NOT_FOUND)
        }

        try {
            userFollowService.getUserFollowings(user, "non-existing")
        } catch (e: FootprinterException) {
            assertThat(e).extracting("errorType").isEqualTo(ErrorType.NOT_FOUND)
        }

        try {
            userFollowService.getUserFollowCount(user, "non-existing")
        } catch (e: FootprinterException) {
            assertThat(e).extracting("errorType").isEqualTo(ErrorType.NOT_FOUND)
        }
    }

    // Test getIsFollowingAndFollowed
    @Test
    fun `Could get following and followed information`() {
        // given
        val loginUser = testHelper.createUser(
            username = "login",
            password = "",
        )
        val targetUser = testHelper.createUser(
            username = "target",
            password = "",
        )

        val expectedUserFollowingResponse = UserFollowingResponse(false, false)

        // when
        val actualUserFollowingResponse = userFollowService.getIsFollowingAndFollowed(loginUser, "target")

        // then
        assertThat(actualUserFollowingResponse).isEqualTo(expectedUserFollowingResponse)
    }

    @Test
    fun `Throw NOT_FOUND when targetUser doesn't exists from getIsFollowingAndFollowed`() {
        // given
        val loginUser = testHelper.createUser(
            username = "login",
            password = "",
        )

        // when // then
        val exception = assertThrows<FootprinterException> {
            userFollowService.getIsFollowingAndFollowed(loginUser, "nonExists")
        }
        assertEquals(exception.errorType, ErrorType.NOT_FOUND)
    }
}
