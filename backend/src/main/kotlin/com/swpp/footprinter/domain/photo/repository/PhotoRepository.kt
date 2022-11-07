package com.swpp.footprinter.domain.photo.repository

import com.swpp.footprinter.domain.photo.model.Photo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PhotoRepository : JpaRepository<Photo, Long> {
    fun findByImagePath(imagePath: String): Photo?
}
