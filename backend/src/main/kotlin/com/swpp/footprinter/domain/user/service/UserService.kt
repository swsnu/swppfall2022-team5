package com.swpp.footprinter.domain.user.service

import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.domain.trace.dto.TraceDetailResponse
import com.swpp.footprinter.domain.trace.dto.TraceResponse
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.dto.UserResponse
import com.swpp.footprinter.domain.user.model.User
import com.swpp.footprinter.domain.user.model.UserFollow
import com.swpp.footprinter.domain.user.repository.UserFollowRepository
import com.swpp.footprinter.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface UserService {
    fun getUserTraces(userId: Long): List<TraceResponse>
    fun getUserTraceByDate(userId: Long, date: String): TraceDetailResponse?
    fun getUserFollowers(loginUser: User, username: String): List<UserResponse>
    fun getUserFollowings(loginUser: User, username: String): List<UserResponse>
    fun followUser(loginUser: User, followedUserName: String)
    fun unfollowUser(loginUser: User, unfollowedUserName: String)
    fun deleteFollower(loginUser: User, followerName: String)
}

@Service
class UserServiceImpl(
    private val userFollowRepo: UserFollowRepository,
    private val userRepo: UserRepository,
    private val traceRepo: TraceRepository,
) : UserService {
    override fun getUserTraces(userId: Long): List<TraceResponse> {
        val user = userRepo.findByIdOrNull(userId) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        val traces = traceRepo.findTraceAllByOwner(user)
        return traces.map { it.toResponse() }
    }

    override fun getUserTraceByDate(userId: Long, date: String): TraceDetailResponse? {
        val user = userRepo.findByIdOrNull(userId) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        return traceRepo.findTraceByOwnerAndTraceDate(user, date).first().toDetailResponse()
    }

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

    override fun followUser(loginUser: User, followedUserName: String) {
        val followedUser = userRepo.findByUsername(followedUserName) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        val newFollow = UserFollow(follower = loginUser, followed = followedUser)

        userFollowRepo.save(newFollow)
    }

    override fun unfollowUser(loginUser: User, unfollowedUserName: String) {
        val unfollowedUser = userRepo.findByUsername(unfollowedUserName) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        val follow = userFollowRepo.findUserFollowByFollowerAndFollowed(follower = loginUser, followed = unfollowedUser) ?: throw FootprinterException(ErrorType.NOT_FOUND)

        userFollowRepo.delete(follow)
    }

    override fun deleteFollower(loginUser: User, followerName: String) {
        val followerUser = userRepo.findByUsername(followerName) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        val follow = userFollowRepo.findUserFollowByFollowerAndFollowed(follower = followerUser, followed = loginUser) ?: throw FootprinterException(ErrorType.NOT_FOUND)

        userFollowRepo.delete(follow)
    }
}
