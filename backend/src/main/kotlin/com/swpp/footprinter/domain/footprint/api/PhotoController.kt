package com.swpp.footprinter.domain.footprint.api

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/photos")
class PhotoController {

    @PostMapping("/process")
    @ResponseBody
    fun processPhoto(): String {
        return "uniqueId"
    }

    @DeleteMapping("/revert")
    fun deletePhoto(@RequestBody uniqueId: String) {
        println(uniqueId + "!!!")
    }
}
