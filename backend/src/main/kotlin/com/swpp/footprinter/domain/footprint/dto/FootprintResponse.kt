package com.swpp.footprinter.domain.footprint.dto

import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.place.model.Place
import com.swpp.footprinter.domain.tag.model.Tag
import com.swpp.footprinter.domain.trace.model.Trace

data class FootprintResponse(
    val id: Long,
    val startTime: String?,
    val endTime: String?,
    val rating: Int?,
    val memo: String,
    val tag: Tag?,
    val photos: List<Photo>?,
    val place: Place?,
    val trace: Trace?
)
