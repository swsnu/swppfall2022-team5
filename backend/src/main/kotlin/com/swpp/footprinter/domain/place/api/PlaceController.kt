package com.swpp.footprintercreate

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
    private val kakaoApiService: KakaoAPIService
) {

    @GetMapping("/place")
    @ResponseBody
    fun getRegionByCoordinate(
        @RequestParam("longitude") longitude: String,
        @RequestParam("latitude") latitude: String
    ): ResponseEntity<String> {
        return kakaoApiService.coordToRegion(longitude, latitude)
    }
}
