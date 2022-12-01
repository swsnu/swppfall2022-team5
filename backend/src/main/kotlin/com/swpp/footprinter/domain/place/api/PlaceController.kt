package com.swpp.footprinter.domain.place.api

import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.domain.place.dto.PlaceResponse
import com.swpp.footprinter.domain.place.dto.PlaceSearchRequest
import com.swpp.footprinter.domain.place.service.PlaceService
import com.swpp.footprinter.common.externalAPI.KakaoAPIService
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class PlaceController(
    private val kakaoApiService: KakaoAPIService,
    private val placeService: PlaceService,
) {

    @GetMapping("/place")
    @ResponseBody
    fun getRegionByCoordinate(
        @RequestParam("longitude") longitude: String,
        @RequestParam("latitude") latitude: String
    ): ResponseEntity<String> {
        return kakaoApiService.coordToRegion(longitude, latitude)
    }

    @GetMapping("/place/search")
    @ResponseBody
    fun getPlacesSearchByKeyword(
        @Valid @RequestBody placeSearchRequest: PlaceSearchRequest,
        bindingResult: BindingResult,
    ): List<PlaceResponse> {
        if (bindingResult.hasErrors()) {
            throw FootprinterException(ErrorType.WRONG_FORMAT)
        }
        return placeService.searchPlacesByKeywordAndTags(placeSearchRequest)
    }
}
