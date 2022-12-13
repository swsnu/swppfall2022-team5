package com.swpp.footprinter.domain.photo.api

import com.amazonaws.services.s3.AmazonS3Client

import com.swpp.footprinter.domain.photo.service.PhotoService

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/photos")
class PhotoController(
    private val photoService: PhotoService,
    private val amazonS3Client: AmazonS3Client,

    @Value("\${cloud.aws.s3.bucket-name}")
    private val bucketName: String
) {

    @PostMapping("/process")
    @ResponseBody
    fun processPhoto(@RequestPart(name = "files") multipartFiles: List<MultipartFile>): String {
        val path = photoService.saveMultipartFileToS3(multipartFiles)
        photoService.processMetadataAndSaveAsPhoto(multipartFiles, path)
        return path
    }

    @DeleteMapping("/revert")
    fun deletePhoto(@RequestBody uniquePath: String): ResponseEntity<Any> {
        amazonS3Client.deleteObject(bucketName, uniquePath)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/user/process")
    @ResponseBody
    fun processUserPhoto(@RequestPart(name = "files") multipartFiles: List<MultipartFile>): String {
        return photoService.saveMultipartFileToS3(multipartFiles)
    }

    @DeleteMapping("/user/revert")
    fun deleteUserPhoto(@RequestBody uniquePath: String): ResponseEntity<Any> {
        amazonS3Client.deleteObject(bucketName, uniquePath)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
