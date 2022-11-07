package com.swpp.footprinter.common.utils

import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.TemporalAccessor
import java.util.*

fun dateToString8601(date: Date): String {
    val instant = date.toInstant()
    val string = DateTimeFormatter.ISO_INSTANT.format(instant)
    return string
}
fun stringToDate8601(string: String): Date {
    try {
        val ta: TemporalAccessor = DateTimeFormatter.ISO_INSTANT.parse(string)
        val i: Instant = Instant.from(ta)
        val d = Date.from(i)
        return d
    } catch (e: DateTimeParseException) {
        throw FootprinterException(ErrorType.WRONG_FORMAT)
    }
}
