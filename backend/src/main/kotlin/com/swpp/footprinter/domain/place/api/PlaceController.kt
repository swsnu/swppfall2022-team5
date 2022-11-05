package com.swpp.footprinter.domain.place.api

import com.swpp.footprinter.domain.place.service.externalAPI.KakaoAPIService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class PlaceController(
    private val apiService: KakaoAPIService
) {

    @GetMapping("/place")
    @ResponseBody
    fun getRegionByCoordinate(
        @RequestParam("x") longitude: String,
        @RequestParam("y") latitude: String
    ): ResponseEntity<String> {
        return apiService.coordToRegion(longitude, latitude)
    }
}
