package com.swpp.footprinter.domain.trace.repository

import com.swpp.footprinter.domain.trace.model.Trace
import com.swpp.footprinter.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface TraceRepository : JpaRepository<Trace, Long> {
    fun findTraceByOwnerAndTraceDate(owner: User, traceDate: String): Trace?
}
