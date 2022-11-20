package com.swpp.footprinter.domain.tag.repository

import com.swpp.footprinter.domain.tag.TAG_CODE
import com.swpp.footprinter.domain.tag.model.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, Long> {
    fun findByTagCode(code: TAG_CODE): Tag?
    fun existsByTagCode(code: TAG_CODE): Boolean
}
