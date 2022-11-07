package com.swpp.footprinter.domain.tag.repository

import com.swpp.footprinter.domain.tag.model.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long> {
    fun findByTagName(name: String): Tag?
}
