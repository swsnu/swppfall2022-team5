package com.swpp.footprinter.domain.place.service.externalAPI

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class KakaoAPIService (
    @Value("\${kakao_apikey}")
    private val kakao_apikey: String
) {
    private val url = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json"

    fun coordToRegion(longitude: String, latitude: String): ResponseEntity<String> {
        val restTemplate = RestTemplate()
        val httpHeaders = HttpHeaders()
        httpHeaders.set("Authorization", "KakaoAK $kakao_apikey")

        val request = HttpEntity<String>(httpHeaders)
        val response: ResponseEntity<String> = restTemplate.exchange("$url?x=$longitude&y=$latitude", HttpMethod.GET, request, String::class)

        return response
    }
}
