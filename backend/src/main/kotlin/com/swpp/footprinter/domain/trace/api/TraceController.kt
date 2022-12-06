package com.swpp.footprinter.domain.trace.api

import com.swpp.footprinter.common.annotations.UserContext
import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.domain.footprint.dto.FootprintInitialTraceResponse
import com.swpp.footprinter.domain.trace.dto.TraceDetailResponse
import com.swpp.footprinter.domain.trace.dto.TraceRequest
import com.swpp.footprinter.domain.trace.dto.TraceSearchRequest
import com.swpp.footprinter.domain.trace.dto.TraceViewResponse
import com.swpp.footprinter.domain.trace.service.TraceService
import com.swpp.footprinter.domain.user.model.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/traces")
class TraceController(
    private val traceService: TraceService
) {

    @GetMapping("/user/{username}")
    @ResponseBody
    fun getTraces(
        @UserContext loginUser: User,
        @PathVariable username: String
    ): List<TraceDetailResponse> {
        return traceService.getAllUserTraces(loginUser, username)
    }

    @GetMapping("/explore")
    @ResponseBody
    fun getOtherUsersTraces(@UserContext loginUser: User): List<TraceDetailResponse> {
        return traceService.getAllOtherUsersTraces(loginUser)
    }

    @GetMapping("/date/{date}")
    @ResponseBody
    fun getTraceByDate(
        @PathVariable date: String,
        @UserContext loginUser: User
    ): TraceDetailResponse? {
        return traceService.getTraceByDate(date, loginUser)
    }

    @PostMapping
    fun createTrace(
        @RequestBody @Valid request: TraceRequest,
        @UserContext loginUser: User
    ): ResponseEntity<String> {
        traceService.createTrace(request, loginUser)
        return ResponseEntity<String>("Created", HttpStatus.CREATED)
    }

    @GetMapping("/id/{traceId}")
    fun getTraceDetail(
        @UserContext user: User,
        @PathVariable(name = "traceId", required = true) traceId: Long
    ): TraceDetailResponse {
        return traceService.getTraceById(traceId, user.id)
    }

    @DeleteMapping("/id/{traceId}")
    fun deleteTrace(
        @PathVariable(required = true) traceId: Long,
        @UserContext loginUser: User
    ) {
        traceService.deleteTraceById(traceId, loginUser)
    }

    @PostMapping("/create")
    fun createNewTrace(
        @RequestBody photoPathList: List<String>,
    ): List<FootprintInitialTraceResponse> {
        return traceService.createInitialTraceBasedOnPhotoIdListGiven(photoPathList)
    }

    @GetMapping("/search")
    fun searchTrace(
        @RequestBody traceSearchRequest: TraceSearchRequest,
        bindingResult: BindingResult,
    ): List<TraceDetailResponse> {
        if (bindingResult.hasErrors()) {
            throw FootprinterException(ErrorType.WRONG_FORMAT)
        }
        return traceService.searchTrace(traceSearchRequest)
    }

    @PostMapping("/view/{traceId}")
    fun updateTraceViewCount(
        @PathVariable(required = true) traceId: Long,
    ): TraceViewResponse {
        return traceService.updateViewCount(traceId)
    }
}
