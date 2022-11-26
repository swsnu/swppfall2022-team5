package com.swpp.footprinter.domain.user.service

import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.domain.user.dto.UserFollowResponse
import com.swpp.footprinter.domain.user.dto.UserResponse
import com.swpp.footprinter.domain.user.model.User
import com.swpp.footprinter.domain.user.model.UserFollow
import com.swpp.footprinter.domain.user.repository.UserFollowRepository
import com.swpp.footprinter.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface UserFollowService {
    fun getUserFollowers(loginUser: User, username: String): List<UserResponse>
    fun getUserFollowings(loginUser: User, username: String): List<UserResponse>
    fun followUser(loginUser: User, followedUserName: String)
    fun unfollowUser(loginUser: User, unfollowedUserName: String)
    fun deleteFollower(loginUser: User, followerName: String)
    fun getUserFollowCount(loginUser: User, username: String): UserFollowResponse
}

@Service
class UserFollowServiceImpl(
    private val userRepo: UserRepository,
    private val userFollowRepo: UserFollowRepository,
) : UserFollowService {

    override fun getUserFollowers(loginUser: User, username: String): List<UserResponse> {
        val user = userRepo.findByUsername(username) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        val followList = userFollowRepo.findUserFollowsByFollowed(user)
        return followList.map { UserResponse(it.follower.username) }
    }

    override fun getUserFollowings(loginUser: User, username: String): List<UserResponse> {
        val user = userRepo.findByUsername(username) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        val followList = userFollowRepo.findUserFollowsByFollower(user)
        return followList.map { UserResponse(it.followed.username) }
    }

    @Transactional
    override fun followUser(loginUser: User, followedUserName: String) {
        val followedUser = userRepo.findByUsername(followedUserName) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        val newFollow = UserFollow(follower = loginUser, followed = followedUser)

        userFollowRepo.save(newFollow)

        loginUser.followingCount += 1
        followedUser.followerCount += 1
    }

    @Transactional
    override fun unfollowUser(loginUser: User, unfollowedUserName: String) {
        val unfollowedUser = userRepo.findByUsername(unfollowedUserName) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        val follow = userFollowRepo.findUserFollowByFollowerAndFollowed(follower = loginUser, followed = unfollowedUser) ?: throw FootprinterException(
            ErrorType.NOT_FOUND
        )

        userFollowRepo.delete(follow)

        loginUser.followingCount -= 1
        unfollowedUser.followerCount -= 1
    }

    @Transactional
    override fun deleteFollower(loginUser: User, followerName: String) {
        val followerUser = userRepo.findByUsername(followerName) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        val follow = userFollowRepo.findUserFollowByFollowerAndFollowed(follower = followerUser, followed = loginUser) ?: throw FootprinterException(
            ErrorType.NOT_FOUND
        )

        userFollowRepo.delete(follow)

        loginUser.followerCount -= 1
        followerUser.followingCount -= 1
    }

    override fun getUserFollowCount(loginUser: User, username: String): UserFollowResponse {
        val user = userRepo.findByUsername(username) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        return UserFollowResponse(followerCount = user.followerCount, followingCount = user.followingCount)
    }
}
