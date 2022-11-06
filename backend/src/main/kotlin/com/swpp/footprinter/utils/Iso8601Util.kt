package com.swpp.footprinter.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*

fun dateToString8601(date: Date): String {
    val instant = date.toInstant()
    val string = DateTimeFormatter.ISO_INSTANT.format(instant)
    return string
}
fun stringToDate8601(string: String): Date {
    val ta: TemporalAccessor = DateTimeFormatter.ISO_INSTANT.parse(string)
    val i: Instant = Instant.from(ta)
    val d = Date.from(i)
    return d
}
