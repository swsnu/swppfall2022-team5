package com.swpp.footprinter.domain.footprint.dto

import com.swpp.footprinter.domain.photo.dto.PhotoRequest
import com.swpp.footprinter.domain.place.dto.PlaceRequest
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
    val tagId: Int?,
    @field: NotEmpty
    val photos: List<PhotoRequest>,
    @field: NotNull
    val place: PlaceRequest?
)
