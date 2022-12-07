package com.swpp.footprinter.domain.user.service

import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.common.utils.ImageUrlUtil
import com.swpp.footprinter.domain.trace.dto.TraceDetailResponse
import com.swpp.footprinter.domain.trace.dto.TraceResponse
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.dto.UserResponse
import com.swpp.footprinter.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface UserService {
    fun getUserTraces(userId: Long): List<TraceResponse>
    fun getUserTraceByDate(userId: Long, date: String): TraceDetailResponse?

    fun getUserByUsername(username: String): UserResponse
}

@Service
class UserServiceImpl(
    private val userRepo: UserRepository,
    private val traceRepo: TraceRepository,
    private val imageUrlUtil: ImageUrlUtil,
) : UserService {
    override fun getUserTraces(userId: Long): List<TraceResponse> {
        val user = userRepo.findByIdOrNull(userId) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        val traces = traceRepo.findTraceAllByOwner(user)
        return traces.map { it.toResponse() }
    }

    override fun getUserTraceByDate(userId: Long, date: String): TraceDetailResponse? {
        val user = userRepo.findByIdOrNull(userId) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        return traceRepo.findTracesByTraceDate(date).first().toDetailResponse(imageUrlUtil)
    }

    override fun getUserByUsername(username: String): UserResponse {
        val user = userRepo.findByUsername(username) ?: throw FootprinterException(ErrorType.NOT_FOUND)
        return user.toResponse()
    }
}
