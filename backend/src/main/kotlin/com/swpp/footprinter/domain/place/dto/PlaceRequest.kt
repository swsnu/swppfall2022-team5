package com.swpp.footprinter.domain.place.dto

import javax.validation.constraints.NotBlank

data class PlaceRequest(
    @field: NotBlank
    val name: String? = null,

    @field: NotBlank
    val address: String? = null,

//    @field: NotEmpty
//    val tag: String? = null
//    TODO: Maybe...?
)
