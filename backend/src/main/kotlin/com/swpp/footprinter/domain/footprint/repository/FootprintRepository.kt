package com.swpp.footprinter.domain.footprint.repository

import com.swpp.footprinter.domain.footprint.model.Footprint
import org.springframework.data.jpa.repository.JpaRepository

interface FootprintRepository : JpaRepository<Footprint, Long>
