package com.swpp.footprinter.domain.trace.service

import com.swpp.footprinter.domain.footprint.model.Footprint
import com.swpp.footprinter.domain.footprint.repository.FootprintRepository
import com.swpp.footprinter.domain.trace.dto.TraceRequest
import com.swpp.footprinter.domain.trace.dto.TraceResponse
import com.swpp.footprinter.domain.trace.model.Trace
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface TraceService {
    fun getAllTraces() : List<TraceResponse>
    fun createTrace(request: TraceRequest)
}

@Service
class TraceServiceImpl(
    private val traceRepo : TraceRepository,
    private val footprintRepo: FootprintRepository,
    private val userRepo: UserRepository
) : TraceService {
    override fun getAllTraces(): List<TraceResponse> {
        return traceRepo.findAll().filter { it.owner != userRepo.findByIdOrNull(1)!!  }.map { trace -> trace.toResponse() } // TODO: 현재 user로 넣기
    }

    override fun createTrace(request: TraceRequest) {
        val newTrace = Trace(
            traceTitle = request.title!!,
            traceDate = request.date!!,
            owner = userRepo.findByIdOrNull(3)!!, // TODO: 현재 user로 넣기
            footprintList = listOf()
        )
        val newId = traceRepo.save(newTrace).id
        for (footprint in request.footprintList!!) {
            footprintRepo.save(Footprint(
                startTime = footprint.startTime!!,
                endTime = footprint.endTime!!,
                rating = footprint.rating!!,
                trace = newTrace,
                place = footprint.place!!,
                tag = footprint.tag!!,
                memo = footprint.memo!!,
                photos = footprint.photos!!
            ))
        }
    }
}
