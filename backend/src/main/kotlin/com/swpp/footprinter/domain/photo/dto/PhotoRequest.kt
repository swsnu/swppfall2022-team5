package com.swpp.footprinter.domain.photo.dto

import javax.validation.constraints.NotEmpty

data class PhotoRequest(
    @field: NotEmpty
    val imagePath: String?,

    // @field: NotNull
    val longitude: Double?,

    // @field: NotNull
    val latitude: Double?,

    // Check manually in Iso8601Util
    // @field: NotEmpty
    val timestamp: String?
)
