package com.swpp.footprinter.domain.user.service

import com.amazonaws.services.s3.AmazonS3Client
import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.common.utils.ImageUrlUtil
import com.swpp.footprinter.domain.trace.dto.TraceDetailResponse
import com.swpp.footprinter.domain.trace.dto.TraceResponse
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.dto.UserModifyRequest
import com.swpp.footprinter.domain.user.dto.UserResponse
import com.swpp.footprinter.domain.user.model.User
import com.swpp.footprinter.domain.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface UserService {
    fun getUserTraces(userId: Long): List<TraceResponse>
    fun getUserTraceByDate(userId: Long, date: String): TraceDetailResponse?

    fun getUserByUsername(username: String): UserResponse
    fun modifyUser(loginUser: User, userModifyRequest: UserModifyRequest)
}

@Service
class UserServiceImpl(
    private val userRepo: UserRepository,
    private val traceRepo: TraceRepository,
    private val imageUrlUtil: ImageUrlUtil,
    private val passwordEncoder: PasswordEncoder,
    private val amazonS3Client: AmazonS3Client,
    @Value("\${cloud.aws.s3.bucket-name}")
    private val bucketName: String,
) : UserService {
    override fun getUserTraces(userId: Long): List<TraceResponse> {
        val user = userRepo.findByIdOrNull(userId) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        val traces = traceRepo.findTraceAllByOwner(user)
        return traces.map { it.toResponse(imageUrlUtil) }
    }

    override fun getUserTraceByDate(userId: Long, date: String): TraceDetailResponse? {
        val user = userRepo.findByIdOrNull(userId) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        return traceRepo.findTracesByTraceDate(date).first().toDetailResponse(imageUrlUtil)
    }

    override fun getUserByUsername(username: String): UserResponse {
        val user = userRepo.findByUsername(username) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        return user.toResponse(imageUrlUtil)
    }

    @Transactional
    override fun modifyUser(loginUser: User, userModifyRequest: UserModifyRequest) {
        // Update password
        loginUser.password = passwordEncoder.encode(userModifyRequest.password)
        // Update profile image, and remove previous image from server if different
        if (loginUser.imagePath != null && loginUser.imagePath != userModifyRequest.imagePath) {
            amazonS3Client.deleteObject(bucketName, loginUser.imagePath)
        }
        loginUser.imagePath = userModifyRequest.imagePath
        userRepo.save(loginUser)
    }
}
