package com.swpp.footprinter.domain.trace.service

import com.swpp.footprinter.domain.trace.model.TraceLike
import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.domain.trace.repository.TraceLikeRepository
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.model.User
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface TraceLikeService {
    fun likeTraceById(user: User, traceId: Long): Int
    fun unlikeTraceById(user: User, traceId: Long): Int
}

@Service
class TraceLikeServiceImpl(
    private val traceRepository: TraceRepository,
    private val traceLikeRepository: TraceLikeRepository
) : TraceLikeService {

//    @Transactional
    override fun likeTraceById(user: User, traceId: Long): Int {
        val trace = try { traceRepository.getReferenceById(traceId) } catch (e: JpaObjectRetrievalFailureException) { throw FootprinterException(ErrorType.NOT_FOUND) }
        val newTraceLike = TraceLike(
            trace = trace,
            user = user
        )
        try {
            traceLikeRepository.save(newTraceLike)
        } catch (e: DataIntegrityViolationException) {
            return trace.likesCount
        }
        trace.likesCount += 1
        traceRepository.save(trace)
        return trace.likesCount
    }

    @Transactional
    override fun unlikeTraceById(user: User, traceId: Long): Int {
        val trace = try { traceRepository.getReferenceById(traceId) } catch (e: JpaObjectRetrievalFailureException) { throw FootprinterException(ErrorType.NOT_FOUND) }
        val traceLike = traceLikeRepository.findByTraceIdAndUserId(traceId, user.id) ?: return trace.likesCount
        trace.likesCount -= 1
        traceRepository.save(trace)
        traceLikeRepository.delete(traceLike)
        return trace.likesCount
    }
}
