package com.swpp.footprinter.domain.trace.api

import com.swpp.footprinter.domain.footprint.dto.FootprintInitialTraceResponse
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

    @PostMapping("/traces")
    fun createTrace(
        @RequestBody @Valid request: TraceRequest
    ): ResponseEntity<String> {
        service.createTrace(request)
        return ResponseEntity<String>("Created", HttpStatus.OK)
    }

    @GetMapping("/traces/{traceId}")
    fun getTraceDetail(
        @PathVariable traceId: Long
    ): TraceResponse {
        return service.getTraceById(traceId)
    }

    @DeleteMapping("/traces/{traceId}")
    fun deleteTrace(
        @PathVariable traceId: Long
    ) {
        service.deleteTraceById(traceId)
    }

    @PostMapping("/traces/create")
    fun createNewTrace(
        @RequestBody photoIdLists: List<Long>,
    ): List<FootprintInitialTraceResponse> {
        return service.createInitialTraceBasedOnPhotoIdListGiven(photoIdLists)
    }
}
