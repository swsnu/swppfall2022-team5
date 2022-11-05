package com.swpp.footprinter.domain.footprint.api

import com.swpp.footprinter.domain.footprint.dto.FootprintRequest
import com.swpp.footprinter.domain.footprint.dto.FootprintResponse
import com.swpp.footprinter.domain.footprint.service.FootprintService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class FootprintController(
    private val service: FootprintService
) {

    @GetMapping("/footprints/{footprintId}")
    @ResponseBody
    fun findFootprintById(
        @PathVariable footprintId: Long
    ): FootprintResponse {
        return service.getFootprintById(footprintId)
    }

    @PutMapping("/footprints/{footprintId}")
    fun editFootprintById(
        @PathVariable footprintId: Long,
        @RequestBody @Valid request: FootprintRequest
    ) {
        service.editFootprint(footprintId, request)
    }

    @DeleteMapping("/footprints/{footprintId}")
    fun deleteFootprintById(
        @PathVariable footprintId: Long
    ) {
        service.deleteFootprintById(footprintId)
    }
}
