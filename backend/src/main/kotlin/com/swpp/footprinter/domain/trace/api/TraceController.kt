package com.swpp.footprinter.domain.trace.api

import com.swpp.footprinter.common.annotations.UserContext
import com.swpp.footprinter.domain.footprint.dto.FootprintInitialTraceResponse
import com.swpp.footprinter.domain.trace.dto.TraceDetailResponse
import com.swpp.footprinter.domain.trace.dto.TraceRequest
import com.swpp.footprinter.domain.trace.service.TraceService
import com.swpp.footprinter.domain.user.model.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/traces")
class TraceController(
    private val service: TraceService
) {

    @GetMapping("/user/{username}")
    @ResponseBody
    fun getTraces(
        @UserContext loginUser: User,
        @PathVariable username: String
    ): List<TraceDetailResponse> {
        return service.getAllUserTraces(loginUser, username)
    }

    @GetMapping("/explore")
    @ResponseBody
    fun getOtherUsersTraces(@UserContext loginUser: User): List<TraceDetailResponse> {
        return service.getAllOtherUsersTraces(loginUser)
    }

    @GetMapping("/date/{date}")
    @ResponseBody
    fun getTraceByDate(
        @PathVariable date: String,
        @UserContext loginUser: User
    ): TraceDetailResponse? {
        return service.getTraceByDate(date, loginUser)
    }

    @PostMapping
    fun createTrace(
        @RequestBody @Valid request: TraceRequest,
        @UserContext loginUser: User
    ): ResponseEntity<String> {
        service.createTrace(request, loginUser)
        return ResponseEntity<String>("Created", HttpStatus.CREATED)
    }

    @GetMapping("/id/{traceId}")
    fun getTraceDetail(
        @PathVariable(name = "traceId", required = true) traceId: Long
    ): TraceDetailResponse {
        return service.getTraceById(traceId)
    }

    @DeleteMapping("/id/{traceId}")
    fun deleteTrace(
        @PathVariable(required = true) traceId: Long,
        @UserContext loginUser: User
    ) {
        service.deleteTraceById(traceId, loginUser)
    }

    @PostMapping("/create")
    fun createNewTrace(
        @RequestBody photoPathList: List<String>,
    ): List<FootprintInitialTraceResponse> {
        return service.createInitialTraceBasedOnPhotoIdListGiven(photoPathList)
    }
}
