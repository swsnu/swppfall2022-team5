package com.swpp.footprinter.domain.place.service

import com.swpp.footprinter.domain.place.dto.PlaceResponse
import com.swpp.footprinter.domain.place.dto.PlaceSearchRequest
import com.swpp.footprinter.common.externalAPI.KakaoAPIService
import org.springframework.stereotype.Service
import com.swpp.footprinter.common.PLACE_SEARCH_METER
import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.domain.tag.TAG_CODE

interface PlaceService {
    fun getAddressByLatLon(latitude: String, longitude: String): String
    fun searchPlacesByKeywordAndTags(placeSearchRequest: PlaceSearchRequest): List<PlaceResponse>
}

@Service
class PlaceServiceImpl(
    private val kakaoAPIService: KakaoAPIService,
) : PlaceService {
    override fun getAddressByLatLon(latitude: String, longitude: String): String {
        val response = kakaoAPIService.coordToRegion(longitude, latitude)
        val regionList = kakaoAPIService.getDocumentsMapListFromResponse(response)
        return regionList[0].getOrDefault("address_name", "알 수 없음")
    }

    override fun searchPlacesByKeywordAndTags(placeSearchRequest: PlaceSearchRequest): List<PlaceResponse> {
        val searchedPlacesAllTagsResponse = placeSearchRequest.let {
            kakaoAPIService.coordAndKeywordToPlace(
                it.keyword, it.longitude!!, it.latitude!!, PLACE_SEARCH_METER
            )
        }

        val searchedPlacesAllTagsList = kakaoAPIService.getDocumentsMapListFromResponse(searchedPlacesAllTagsResponse)

        return if (placeSearchRequest.tagIDs.isNotEmpty()) {
            val searchTagCodeValueList = placeSearchRequest.tagIDs.map {
                try {
                    TAG_CODE.values()[it].code
                } catch (e: IndexOutOfBoundsException) { // For wrong tag id given
                    throw FootprinterException(ErrorType.WRONG_FORMAT)
                }
            }

            val searchedPlacesWithTagsList = searchedPlacesAllTagsList.filter {
                searchTagCodeValueList.contains(it["category_group_code"])
            }

            searchedPlacesWithTagsList.map {
                PlaceResponse(
                    name = it["place_name"]!!,
                    address = it["address_name"]!!,
                    distance = if (it["distance"]!!.isEmpty()) null else it["distance"]!!.toDouble(),
                    tagId = TAG_CODE.values().find { t -> t.code == it["category_group_code"] }!!.ordinal
                )
            }
        } else {
            searchedPlacesAllTagsList.map {
                PlaceResponse(
                    name = it["place_name"]!!,
                    address = it["address_name"]!!,
                    distance = if (it["distance"]!!.isEmpty()) null else it["distance"]!!.toDouble(),
                )
            }
        }
    }
}
