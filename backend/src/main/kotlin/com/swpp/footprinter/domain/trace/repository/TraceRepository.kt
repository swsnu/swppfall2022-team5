package com.swpp.footprinter.domain.trace.repository

import com.swpp.footprinter.domain.trace.model.Trace
import org.springframework.data.jpa.repository.JpaRepository

interface TraceRepository : JpaRepository<Trace, Long>
