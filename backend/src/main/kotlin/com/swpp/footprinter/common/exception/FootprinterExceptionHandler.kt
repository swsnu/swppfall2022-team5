package com.swpp.footprinter.common.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestControllerAdvice
class FootprinterExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(
        e: Exception,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<Any> {
        val errorType = ErrorType.SERVER_ERROR
        return ResponseEntity(ErrorResponse(ErrorInfo(errorType.code, errorType.message)), errorType.httpStatus)
    }

    @ExceptionHandler(FootprinterException::class)
    fun handlerFootprinterException(e: FootprinterException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(ErrorInfo(e.errorType.code, e.errorType.message)),
            e.errorType.httpStatus,
        )
    }
}
