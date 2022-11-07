package com.swpp.footprinter.domain.photo.dto

import org.springframework.format.annotation.DateTimeFormat
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class PhotoRequest(
    @field: NotEmpty
    val imagePath: String?,

    @field: NotNull
    val longitude: Double?,

    @field: NotNull
    val latitude: Double?,

    // Check manually in Iso8601Util
    @field: NotEmpty
    val timestamp: String?
)
