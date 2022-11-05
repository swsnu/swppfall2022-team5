package com.swpp.footprinter.domain.footprint.dto

import com.swpp.footprinter.domain.memo.model.Memo
import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.place.model.Place
import com.swpp.footprinter.domain.tag.model.Tag
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class FootprintRequest(
    @field: NotBlank
    val startTime: String?,
    @field: NotBlank
    val endTime: String?,
    @field: NotNull
    val rating: Int?,
    @field: NotNull
    val memo: Memo?,
    @field: NotNull
    val tag: Tag?,
    @field: NotEmpty
    val photos: List<Photo>?,
    @field: NotNull
    val place: Place?
)
