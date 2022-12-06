package com.swpp.footprinter.domain.user.repository

import com.swpp.footprinter.domain.user.model.User
import com.swpp.footprinter.domain.user.model.UserFollow
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserFollowRepository : JpaRepository<UserFollow, Long> {
    fun findUserFollowByFollowerAndFollowed(follower: User, followed: User): UserFollow?
    fun findUserFollowsByFollowed(followed: User): List<UserFollow>
    fun findUserFollowsByFollower(follower: User): List<UserFollow>

    fun existsByFollowerAndFollowed(follower: User, followed: User): Boolean
}
