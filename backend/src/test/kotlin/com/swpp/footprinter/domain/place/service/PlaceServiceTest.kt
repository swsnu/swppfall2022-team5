package com.swpp.footprinter.domain.place.service

import com.swpp.footprinter.common.externalAPI.KakaoAPIService
import com.swpp.footprinter.domain.place.dto.PlaceResponse
import com.swpp.footprinter.domain.place.dto.PlaceSearchRequest
import com.swpp.footprinter.domain.tag.TAG_CODE
import net.minidev.json.JSONObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.anyString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.junit.jupiter.api.Assertions.*
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyInt

@SpringBootTest
class PlaceServiceTest @Autowired constructor(
    private val placeService: PlaceService,
    @MockBean val mockKakaoAPIService: KakaoAPIService,
) {

    // Testing getAddressByLatLon
    @Test
    fun `Could get address by latitude and longitude`() {
        // given
        val responseMock = ResponseEntity(
            JSONObject.toJSONString(
                mapOf(
                    "documents" to listOf(
                        mapOf(
                            "address_name" to "hello",
                        ),
                    )
                )
            ),
            HttpStatus.OK
        )

        // when
        `when`(mockKakaoAPIService.coordToRegion(anyString(), anyString()))
            .thenReturn(responseMock)
        `when`(mockKakaoAPIService.getDocumentsMapListFromResponse(responseMock))
            .thenCallRealMethod()

        // then
        val testAddress = placeService.getAddressByLatLon("lat", "lon")
        assertEquals(testAddress, "hello")
    }

    @Test
    fun `Could return 알 수 없음 when region cannot be searched`() {
        // given
        val responseMock = ResponseEntity(
            JSONObject.toJSONString(
                mapOf(
                    "documents" to listOf(
                        mapOf(
                            "yeah" to "horray",
                        ),
                    )
                )
            ),
            HttpStatus.OK
        )

        // when
        `when`(mockKakaoAPIService.coordToRegion(anyString(), anyString()))
            .thenReturn(responseMock)
        `when`(mockKakaoAPIService.getDocumentsMapListFromResponse(responseMock))
            .thenCallRealMethod()

        // then
        val testAddress = placeService.getAddressByLatLon("lat", "lon")
        assertEquals(testAddress, "알 수 없음")
    }

    // Test search PlacesByKeywordAndTags
    @Test
    fun `Could return all places related to given tags`() {
        // given
        val placeSearchRequest = PlaceSearchRequest(
            keyword = "hello",
            longitude = 1.0,
            latitude = 1.0,
            tagIDs = listOf(TAG_CODE.음식점.ordinal)
        )
        val responseMock = ResponseEntity(
            JSONObject.toJSONString(
                mapOf(
                    "documents" to
                        listOf(
                            mapOf(
                                "category_group_code" to TAG_CODE.valueOf("음식점").code,
                                "place_name" to "helloPlace",
                                "address_name" to "helloAddress",
                                "distance" to "0",
                            ),
                            mapOf(
                                "category_group_code" to TAG_CODE.valueOf("카페").code,
                                "place_name" to "",
                                "address_name" to "",
                                "distance" to "0",
                            ),
                            mapOf(
                                "category_group_code" to TAG_CODE.valueOf("문화시설").code,
                                "place_name" to "",
                                "address_name" to "",
                                "distance" to "0",
                            ),
                            mapOf(
                                "category_group_code" to TAG_CODE.valueOf("관광명소").code,
                                "place_name" to "",
                                "address_name" to "",
                                "distance" to "0",
                            ),
                            mapOf(
                                "category_group_code" to TAG_CODE.valueOf("숙박").code,
                                "place_name" to "",
                                "address_name" to "",
                                "distance" to "0",
                            ),
                        )
                )
            ),
            HttpStatus.OK
        )

        val expectedPlaceResponseList = listOf(
            PlaceResponse(
                "helloPlace", "helloAddress",
                0.0, TAG_CODE.음식점.ordinal
            )
        )

        `when`(
            mockKakaoAPIService.coordAndKeywordToPlace(
                anyString(), anyDouble(), anyDouble(), anyInt()
            )
        ).thenReturn(responseMock)
        `when`(mockKakaoAPIService.getDocumentsMapListFromResponse(responseMock))
            .thenCallRealMethod()

        // when
        val actualPlaceResponseList = placeService.searchPlacesByKeywordAndTags(placeSearchRequest)

        // then
        assertThat(actualPlaceResponseList).isEqualTo(expectedPlaceResponseList)
    }

    @Test
    fun `Could return places of all tags when empty tagIDs is given`() {
        // given
        val placeSearchRequest = PlaceSearchRequest(
            keyword = "hello",
            longitude = 1.0,
            latitude = 1.0,
            tagIDs = listOf()
        )
        val responseMock = ResponseEntity(
            JSONObject.toJSONString(
                mapOf(
                    "documents" to
                        listOf(
                            mapOf(
                                "category_group_code" to TAG_CODE.valueOf("음식점").code,
                                "place_name" to "food",
                                "address_name" to "food",
                                "distance" to "0",
                            ),
                            mapOf(
                                "category_group_code" to TAG_CODE.valueOf("카페").code,
                                "place_name" to "coffee",
                                "address_name" to "coffee",
                                "distance" to "0",
                            ),
                            mapOf(
                                "category_group_code" to TAG_CODE.valueOf("문화시설").code,
                                "place_name" to "play",
                                "address_name" to "play",
                                "distance" to "0",
                            ),
                            mapOf(
                                "category_group_code" to TAG_CODE.valueOf("관광명소").code,
                                "place_name" to "see",
                                "address_name" to "see",
                                "distance" to "0",
                            ),
                            mapOf(
                                "category_group_code" to TAG_CODE.valueOf("숙박").code,
                                "place_name" to "sleep",
                                "address_name" to "sleep",
                                "distance" to "0",
                            ),
                        )
                )
            ),
            HttpStatus.OK
        )

        val expectedPlaceResponseList = listOf(
            PlaceResponse(
                "food", "food",
                0.0, TAG_CODE.음식점.ordinal
            ),
            PlaceResponse(
                "coffee", "coffee",
                0.0, TAG_CODE.카페.ordinal
            ),
            PlaceResponse(
                "play", "play",
                0.0, TAG_CODE.문화시설.ordinal
            ),
            PlaceResponse(
                "see", "see",
                0.0, TAG_CODE.관광명소.ordinal
            ),
            PlaceResponse(
                "sleep", "sleep",
                0.0, TAG_CODE.숙박.ordinal
            ),
        )

        `when`(
            mockKakaoAPIService.coordAndKeywordToPlace(
                anyString(), anyDouble(), anyDouble(), anyInt()
            )
        ).thenReturn(responseMock)
        `when`(mockKakaoAPIService.getDocumentsMapListFromResponse(responseMock))
            .thenCallRealMethod()

        // when
        val actualPlaceResponseList = placeService.searchPlacesByKeywordAndTags(placeSearchRequest)

        // then
        assertThat(actualPlaceResponseList).isEqualTo(expectedPlaceResponseList)
    }
}
