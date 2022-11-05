package com.swpp.footprinter.domain.photo.api

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Paths
import java.util.*

@RestController
@RequestMapping("/api/v1/photos")
class PhotoController(
    val amazonS3Client: AmazonS3Client,

    @Value("\${cloud.aws.s3.bucket-name}")
    private val bucketName: String
) {

    @PostMapping("/process")
    @ResponseBody
    fun processPhoto(@RequestPart(name = "files") multipartFiles: List<MultipartFile>): String {
        val multipartFile = multipartFiles.firstOrNull() ?: throw FootprinterException(ErrorType.MISSING_REQUEST_BODY)
        val objectMetadata = ObjectMetadata()
        objectMetadata.contentType = multipartFile.contentType
        objectMetadata.contentLength = multipartFile.size
        val randomUniqueId = UUID.randomUUID().toString()
        val path = Paths.get(randomUniqueId, multipartFile.originalFilename).toString()
        amazonS3Client.putObject(PutObjectRequest(bucketName, path, multipartFile.inputStream, objectMetadata))
        return path
    }

    @DeleteMapping("/revert")
    fun deletePhoto(@RequestBody uniquePath: String): ResponseEntity<Any> {
        amazonS3Client.deleteObject(bucketName, uniquePath)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
