package com.swpp.footprinter.common.exception

open class FootprinterException(val errorType: ErrorType) : RuntimeException(errorType.name)
