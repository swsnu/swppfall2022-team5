package com.swpp.footprinter.domain.tag.controller

import com.swpp.footprinter.domain.tag.dto.TagResponse
import com.swpp.footprinter.domain.tag.repository.TagRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/tags")
class TagController(
    private val tagRepo: TagRepository,
) {
    @GetMapping("")
    fun getTagsInfo(): List<TagResponse> {
        return tagRepo
            .findAll()
            .sortedBy { it.tagCode.ordinal }
            .map { it.toResponse() }
    }
}
