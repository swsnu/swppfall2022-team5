package com.swpp.footprinter.domain.trace.dto

import com.swpp.footprinter.domain.footprint.dto.FootprintRequest
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

class TraceRequest(
    @field: NotBlank
    val title: String?,
    val public: Boolean? = true,
    @field: NotEmpty
    val footprintList: List<FootprintRequest>?
)
