package com.swpp.footprinter.domain.trace.api

import com.swpp.footprinter.domain.footprint.dto.FootprintInitialTraceResponse
import com.swpp.footprinter.domain.trace.dto.TraceDetailResponse
import com.swpp.footprinter.domain.trace.dto.TraceRequest
import com.swpp.footprinter.domain.trace.dto.TraceResponse
import com.swpp.footprinter.domain.trace.service.TraceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class TraceController(
    private val service: TraceService
) {

    @GetMapping("/traces")
    @ResponseBody
    fun getTraces(): List<TraceResponse> {
        return service.getAllTraces()
    }

    @GetMapping("/traces/date/{date}")
    @ResponseBody
    fun getTraceByDate(
        @PathVariable date: String
    ): TraceDetailResponse? {
        return service.getTraceByDate(date)
    }

    @PostMapping("/traces")
    fun createTrace(
        @RequestBody @Valid request: TraceRequest
    ): ResponseEntity<String> {
        service.createTrace(request)
        return ResponseEntity<String>("Created", HttpStatus.CREATED)
    }

    @GetMapping("/traces/id/{traceId}")
    fun getTraceDetail(
        @PathVariable(name = "traceId", required = true) traceId: Long
    ): TraceDetailResponse {
        return service.getTraceById(traceId)
    }

    @DeleteMapping("/traces/id/{traceId}")
    fun deleteTrace(
        @PathVariable(required = true) traceId: Long
    ) {
        service.deleteTraceById(traceId)
    }

    @PostMapping("/traces/create")
    fun createNewTrace(
        @RequestBody photoPathList: List<String>,
    ): List<FootprintInitialTraceResponse> {
        return service.createInitialTraceBasedOnPhotoIdListGiven(photoPathList)
    }
}
