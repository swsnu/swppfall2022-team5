package com.swpp.footprinter.domain.footprint.dto

import com.swpp.footprinter.domain.memo.model.Memo
import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.place.model.Place
import com.swpp.footprinter.domain.tag.model.Tag
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class FootprintRequest(
    @field: NotBlank(message = "비어있을 수 없음")
    val startTime: String?,
    @field: NotBlank(message = "비어있을 수 없음")
    val endTime: String?,
    @field: NotNull(message = "비어있을 수 없음")
    val rating: Int?,
    @field: NotNull(message = "비어있을 수 없음")
    val memo: Memo?,
    @field: NotNull(message = "비어있을 수 없음")
    val tag: Tag?,
    @field: NotEmpty(message = "비어있을 수 없음")
    val photos: List<Photo>?,
    @field: NotNull(message = "비어있을 수 없음")
    val place: Place?
)
