package com.swpp.footprinter.common.externalAPI

import com.fasterxml.jackson.databind.ObjectMapper
import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class KakaoAPIService(
    @Value("\${kakao_apikey: default null}")
    private val kakao_apikey: String
) {
    fun coordToRegion(longitude: String, latitude: String): ResponseEntity<String> {
        val url = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json"
        val restTemplate = RestTemplate()
        val httpHeaders = HttpHeaders()
        httpHeaders.set("Authorization", "KakaoAK $kakao_apikey")

        val request = HttpEntity<String>(httpHeaders)

        return try {
            restTemplate.exchange("$url?x=$longitude&y=$latitude", HttpMethod.GET, request, String::class)
        } catch (e: HttpClientErrorException) {
            throw FootprinterException(ErrorType.KAKAOMAP_ERROR)
        }
    }

    // Category code: MT1=대형마트 / CS2=편의점 / PS3=어린이집, 유치원 / SC4=학교 / AC5=학원 / PK6=주차장 / OL7=주유소, 충전소 / SW8=지하철역 / BK9=은행 / CT1=문화시설 / AG2=중개업소 / PO3=공공기관 / AT4=관광명소 / AD5=숙박 / FD6=음식점 / CE7=카페 / HP8=병원 / PM9=약국
    fun coordToPlace(longitude: String, latitude: String, category: String, radius: Int): ResponseEntity<String> {
        val url = "https://dapi.kakao.com/v2/local/search/category.json"
        val restTemplate = RestTemplate()
        val httpHeaders = HttpHeaders()
        httpHeaders.set("Authorization", "KakaoAK $kakao_apikey")

        val request = HttpEntity<String>(httpHeaders)

        return try {
            restTemplate.exchange(
                "$url?category_group_code=$category&x=$longitude&y=$latitude&radius=$radius&sort=distance",
                HttpMethod.GET,
                request,
                String::class
            )
        } catch (e: HttpClientErrorException) {
            throw FootprinterException(ErrorType.KAKAOMAP_ERROR)
        }
    }

    fun coordAndKeywordToPlace(keyword: String, longitude: Double, latitude: Double, radius: Int): ResponseEntity<String> {
        val url = "https://dapi.kakao.com/v2/local/search/keyword.json"
        val restTemplate = RestTemplate()
        val httpHeaders = HttpHeaders()
        httpHeaders.set("Authorization", "KakaoAK $kakao_apikey")

        val request = HttpEntity<String>(httpHeaders)

        return try {
            if (longitude < 0 && latitude < 0) {
                restTemplate.exchange(
                    url = "$url?query=$keyword",
                    method = HttpMethod.GET,
                    requestEntity = request,
                    String::class
                )
            } else {
                restTemplate.exchange(
                    url = "$url?query=$keyword&x=$longitude&y=$latitude&radius=$radius&sort=distance",
                    method = HttpMethod.GET,
                    requestEntity = request,
                    String::class
                )
            }
        } catch (e: HttpClientErrorException) {
            throw FootprinterException(ErrorType.KAKAOMAP_ERROR)
        }
    }

    fun getDocumentsMapListFromResponse(
        response: ResponseEntity<String>
    ): ArrayList<Map<String, String>> {
        val objectMapper = ObjectMapper()
        val body = objectMapper.readValue(response.body, Map::class.java)
        return body["documents"] as ArrayList<Map<String, String>>
    }
}
