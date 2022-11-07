package com.swpp.footprinter.domain.footprint.dto

import com.swpp.footprinter.common.utils.stringToDate8601
import com.swpp.footprinter.domain.footprint.model.Footprint
import com.swpp.footprinter.domain.photo.dto.PhotoRequest
import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.place.dto.PlaceRequest
import com.swpp.footprinter.domain.place.model.Place
import com.swpp.footprinter.domain.tag.model.Tag
import com.swpp.footprinter.domain.trace.model.Trace
import javax.transaction.Transactional
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class FootprintRequest(
    // Checked format afterwards by iso8601 util
    @field: NotBlank
    val startTime: String?,
    @field: NotBlank
    val endTime: String?,

    @field: NotNull
    val rating: Int?,
    @field: NotNull
    val memo: String?,
    @field: NotNull
    val tag: String?,
    @field: NotEmpty
    val photos: List<PhotoRequest>,
    @field: NotNull
    val place: PlaceRequest?
)
