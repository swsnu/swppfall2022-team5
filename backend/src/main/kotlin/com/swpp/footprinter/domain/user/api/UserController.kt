package com.swpp.footprinter.domain.user.api

import com.swpp.footprinter.domain.trace.dto.TraceDetailResponse
import com.swpp.footprinter.domain.trace.dto.TraceResponse
import com.swpp.footprinter.domain.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val service: UserService
) {

    @GetMapping("/{userId}/traces")
    @ResponseBody
    fun getUserTraces(
        @PathVariable userId: Long
    ): List<TraceResponse> {
        return service.getUserTraces(userId)
    }

    @GetMapping("/{userId}/traces/{date}")
    @ResponseBody
    fun getUserTraceByDate(
        @PathVariable userId: Long,
        @PathVariable date: String
    ): TraceDetailResponse? {
        return service.getUserTraceByDate(userId, date)
    }
}
