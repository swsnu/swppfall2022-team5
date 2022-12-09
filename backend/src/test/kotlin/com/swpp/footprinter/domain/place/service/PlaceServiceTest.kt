package com.swpp.footprinter.domain.place.service

import com.swpp.footprinter.common.externalAPI.KakaoAPIService
import net.minidev.json.JSONObject
import org.junit.jupiter.api.Test
import org.mockito.Mockito.anyString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.junit.jupiter.api.Assertions.*

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
}
