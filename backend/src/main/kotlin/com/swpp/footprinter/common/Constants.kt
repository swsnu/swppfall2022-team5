package com.swpp.footprinter.common

val PLACE_GRID_METER = 30
val TIME_GRID_SEC = 3600
val Km_PER_LATLNG_DEGREE = 1112.0

enum class TAG_CODE(val code: String) {
    `문화시설`("CT1"),
    `관광명소`("AT4"),
    `숙박`("AD5"),
    `음식점`("FD6"),
    `카페`("CE7");
}
