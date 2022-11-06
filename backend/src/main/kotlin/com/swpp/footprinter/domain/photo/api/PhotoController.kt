package com.swpp.footprinter.domain.photo.api

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.Metadata
import com.drew.metadata.exif.ExifSubIFDDirectory
import com.drew.metadata.exif.GpsDirectory
import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.photo.repository.PhotoRepository
import com.swpp.footprinter.utils.dateToString8601
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Paths
import java.util.*

@RestController
@RequestMapping("/api/v1/photos")
class PhotoController(
    val amazonS3Client: AmazonS3Client,
    val photoRepository: PhotoRepository,

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
        processMetadataAndSaveAsPhoto(multipartFile, path)
        return path
    }

    @DeleteMapping("/revert")
    fun deletePhoto(@RequestBody uniquePath: String): ResponseEntity<Any> {
        amazonS3Client.deleteObject(bucketName, uniquePath)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    private fun processMetadataAndSaveAsPhoto(multipartFile: MultipartFile, path: String): Photo {
        val file = File(multipartFile.originalFilename ?: throw FootprinterException(ErrorType.MISSING_REQUEST_BODY))
        file.createNewFile()
        val fos = FileOutputStream(file)
        fos.write(multipartFile.bytes)
        fos.close()

        val metadata: Metadata = ImageMetadataReader.readMetadata(file)
        val gpsDirectory: GpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory::class.java)
        val exifDirectory: ExifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory::class.java)

        if (gpsDirectory.containsTag(GpsDirectory.TAG_LATITUDE) && gpsDirectory.containsTag(GpsDirectory.TAG_LONGITUDE)) {
            val pdsLat: Double = gpsDirectory.geoLocation.latitude
            val pdsLon: Double = gpsDirectory.geoLocation.longitude

            val photo = Photo(
                imagePath = path,
                latitude = pdsLat.toString(),
                longitude = pdsLon.toString(),
                timestamp = dateToString8601(exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)),
                footprint = null,
            )
            photoRepository.save(photo)
            file.delete()
            return photo
        } else {
            // TODO : 사진에 위치정보 메타데이터 없는 경우
            return Photo("", "", "", "", null)
        }
    }
}
