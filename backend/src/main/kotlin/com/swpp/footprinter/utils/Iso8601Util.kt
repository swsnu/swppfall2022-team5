package com.swpp.footprinter.utils

import java.text.SimpleDateFormat
import java.util.*

val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.KOREAN)

fun dateToString8601(date: Date): String = format.format(date)
fun stringToDate8601(string: String): Date = format.parse(string)
