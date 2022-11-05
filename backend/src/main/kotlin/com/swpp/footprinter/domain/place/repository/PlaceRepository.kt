package com.swpp.footprinter.domain.place.repository

import com.swpp.footprinter.domain.place.model.Place
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlaceRepository : JpaRepository<Place, Long>
