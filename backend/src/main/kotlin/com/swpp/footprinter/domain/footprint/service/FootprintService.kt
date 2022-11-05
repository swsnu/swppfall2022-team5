package com.swpp.footprinter.domain.footprint.service

import com.swpp.footprinter.domain.footprint.dto.FootprintRequest
import com.swpp.footprinter.domain.footprint.dto.FootprintResponse
import com.swpp.footprinter.domain.footprint.repository.FootprintRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface FootprintService {
    fun getFootprintById(footprintId: Long): FootprintResponse
    fun editFootprint(footprintId: Long, request: FootprintRequest)
    fun deleteFootprintById(footprintId: Long)
}

@Service
class FootprintServiceImpl(
    private val footprintRepo: FootprintRepository
) : FootprintService {
    override fun getFootprintById(footprintId: Long): FootprintResponse {
        val footprint = footprintRepo.findByIdOrNull(footprintId) ?: TODO("존재하지 않는 footprint ID가 요청되면 에러 처리")
        return footprint.toResponse()
    }

    override fun editFootprint(footprintId: Long, request: FootprintRequest) {
        val target = footprintRepo.findByIdOrNull(footprintId) ?: TODO("존재하지 않는 footprint ID가 요청되면 에러 처리")
        target.startTime = request.startTime!!
        target.endTime = request.endTime!!
        target.rating = request.rating!!
        target.memo = request.memo!!
        target.tag = request.tag!!
        target.photos = request.photos!!
        target.place = request.place!!

        footprintRepo.save(target)
    }

    override fun deleteFootprintById(footprintId: Long) {
        footprintRepo.deleteById(footprintId)
    }
}
