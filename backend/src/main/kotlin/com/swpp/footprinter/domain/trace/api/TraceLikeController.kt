package com.swpp.footprinter.domain.trace.api

import com.swpp.footprinter.common.annotations.UserContext
import com.swpp.footprinter.domain.trace.dto.TraceLikeResponse
import com.swpp.footprinter.domain.trace.service.TraceLikeService
import com.swpp.footprinter.domain.user.model.User
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/traces")
class TraceLikeController(
    private val traceLikeService: TraceLikeService
) {
    @PostMapping("/{traceId}/likes")
    @ResponseBody
    fun likeTrace(
        @UserContext user: User,
        @PathVariable traceId: Long
    ): TraceLikeResponse {
        val likesCount = traceLikeService.likeTraceById(user, traceId)
        // 질문: DTO mapping은 Service vs Controller 어디에서 하는게 바람직한가?
        return TraceLikeResponse(likesCount = likesCount)
    }

    @DeleteMapping("/{traceId}/likes")
    @ResponseBody
    fun unlikeTrace(
        @UserContext user: User,
        @PathVariable traceId: Long
    ): TraceLikeResponse {
        val likesCount = traceLikeService.unlikeTraceById(user, traceId)
        return TraceLikeResponse(likesCount = likesCount)
    }
}
