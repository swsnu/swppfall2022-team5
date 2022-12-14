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
        return TraceLikeResponse(likesCount = likesCount, isLiked = true)
    }

    @DeleteMapping("/{traceId}/likes")
    @ResponseBody
    fun unlikeTrace(
        @UserContext user: User,
        @PathVariable traceId: Long
    ): TraceLikeResponse {
        val likesCount = traceLikeService.unlikeTraceById(user, traceId)
        return TraceLikeResponse(likesCount = likesCount, isLiked = false)
    }
}
