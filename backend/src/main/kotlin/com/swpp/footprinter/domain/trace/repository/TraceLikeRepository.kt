package com.swpp.footprinter.domain.trace.repository

import com.swpp.footprinter.domain.trace.model.TraceLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TraceLikeRepository : JpaRepository<TraceLike, Long> {
    fun findByTraceIdAndUserId(traceId: Long, userId: Long): TraceLike?

    fun findAllByTraceId(traceId: Long): MutableList<TraceLike>
}
