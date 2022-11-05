package com.swpp.footprinter.domain.place.service

import com.swpp.footprinter.domain.place.repository.PlaceRepository
import org.springframework.stereotype.Service

interface PlaceService {
}

@Service
class PlaceServiceImpl (
    private val placeRepository: PlaceRepository
) : PlaceService {

}
