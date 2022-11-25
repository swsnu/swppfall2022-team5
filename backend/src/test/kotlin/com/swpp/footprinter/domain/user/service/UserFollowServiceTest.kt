package com.swpp.footprinter.domain.user.service

import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.domain.user.repository.UserRepository
import com.swpp.footprinter.global.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@SpringBootTest
class UserFollowServiceTest @Autowired constructor(
    private val userFollowService: UserFollowService,
    private val testHelper: TestHelper,
    private val userRepo: UserRepository,
) {
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
    fun `Throw NOT_FOUND when follow or unfollow non-existing user`() {
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
    }
}
