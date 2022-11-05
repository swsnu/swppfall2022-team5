package com.swpp.footprinter.domain.trace.dto

import com.swpp.footprinter.domain.photo.model.PhotoInitialTraceDTO
import com.swpp.footprinter.domain.place.model.PlaceInitialTraceDTO

data class InitialTraceDTO(
    val place: PlaceInitialTraceDTO,
    val photoList: List<PhotoInitialTraceDTO>
)
