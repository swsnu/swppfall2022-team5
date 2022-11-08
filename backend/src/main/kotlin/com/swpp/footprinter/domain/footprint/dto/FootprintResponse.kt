package com.swpp.footprinter.domain.footprint.dto

import com.swpp.footprinter.domain.photo.dto.PhotoResponse
import com.swpp.footprinter.domain.place.dto.PlaceResponse
import com.swpp.footprinter.domain.tag.dto.TagResponse

data class FootprintResponse(
    val id: Long,
    val startTime: String,
    val endTime: String,
    val rating: Int,
    val memo: String,
    val tag: TagResponse,
    val photos: List<PhotoResponse>,
    val place: PlaceResponse,
    val traceId: Long
)
