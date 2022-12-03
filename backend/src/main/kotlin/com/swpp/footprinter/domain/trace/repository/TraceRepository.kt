package com.swpp.footprinter.domain.trace.repository

import com.swpp.footprinter.domain.trace.model.Trace
import com.swpp.footprinter.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TraceRepository : JpaRepository<Trace, Long> {
    fun findByOwnerAndTraceDate(owner: User, traceDate: String): Trace?
    fun findTracesByTraceDate(traceDate: String): MutableList<Trace>
    fun findTraceAllByOwner(user: User): MutableList<Trace>
    fun existsByTraceTitle(title: String): Boolean
    fun findByTraceTitle(title: String): Trace?
}
