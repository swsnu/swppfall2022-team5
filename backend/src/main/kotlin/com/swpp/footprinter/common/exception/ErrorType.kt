package com.swpp.footprinter.common.exception

import org.springframework.http.HttpStatus

enum class ErrorType(
    val code: Int,
    val httpStatus: HttpStatus,
    val message: String
) {
    WRONG_FORMAT(4000, HttpStatus.BAD_REQUEST, "양식이 잘못되었습니다."),
    MISSING_REQUEST_BODY(4001, HttpStatus.BAD_REQUEST, "요청 데이터가 존재하지 않습니다."),
    NOT_FOUND(4004, HttpStatus.NOT_FOUND, "서버상에 데이터가 존재하지 않습니다."),
    SERVER_ERROR(5001, HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    S3_UPLOAD_ERROR(5002, HttpStatus.INTERNAL_SERVER_ERROR, "미디어 업로드가 실패했습니다."),
}
