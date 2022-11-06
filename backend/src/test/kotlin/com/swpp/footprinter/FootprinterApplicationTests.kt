package com.swpp.footprinter

import com.swpp.footprinter.test.DummyService
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FootprinterApplicationTests {

    @Test
    fun dummyServiceTest() {
        val service = DummyService()
        assert(service.returnDummyValue() == "hello")
    }
}
