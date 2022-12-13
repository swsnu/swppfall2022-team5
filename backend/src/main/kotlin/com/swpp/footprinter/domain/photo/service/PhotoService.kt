package com.swpp.footprinter.domain.photo.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifSubIFDDirectory
import com.drew.metadata.exif.GpsDirectory
import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.photo.repository.PhotoRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import com.drew.metadata.Metadata
import com.swpp.footprinter.common.NO_META_DATA
import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import org.springframework.beans.factory.annotation.Value
import java.nio.file.Paths
import java.util.*
import javax.transaction.Transactional

interface PhotoService {
    fun saveMultipartFileToS3(multipartFiles: List<MultipartFile>): String
    fun processMetadataAndSaveAsPhoto(multipartFiles: List<MultipartFile>, path: String)
    fun deletePhotoFromDatabaseAndServer(photo: Photo)
}

@Service
class PhotoServiceImpl(
    private val photoRepo: PhotoRepository,
    private val amazonS3Client: AmazonS3Client,

    @Value("\${cloud.aws.s3.bucket-name}")
    private val bucketName: String
) : PhotoService {

    override fun saveMultipartFileToS3(multipartFiles: List<MultipartFile>): String {
        val multipartFile = multipartFiles.firstOrNull() ?: throw FootprinterException(ErrorType.MISSING_REQUEST_BODY)
        val objectMetadata = ObjectMetadata()
        objectMetadata.contentType = multipartFile.contentType
        objectMetadata.contentLength = multipartFile.size
        val randomUniqueId = UUID.randomUUID().toString()
        val path = Paths.get(randomUniqueId, multipartFile.originalFilename).toString()
        amazonS3Client.putObject(PutObjectRequest(bucketName, path, multipartFile.inputStream, objectMetadata))
        return path
    }

    @Transactional
    override fun processMetadataAndSaveAsPhoto(multipartFiles: List<MultipartFile>, path: String) {
        val multipartFile = multipartFiles.firstOrNull() ?: throw FootprinterException(ErrorType.MISSING_REQUEST_BODY)
        val metadata: Metadata = ImageMetadataReader.readMetadata(multipartFile.inputStream)
        val gpsDirectory: GpsDirectory? = metadata.getFirstDirectoryOfType(GpsDirectory::class.java)
        val exifDirectory: ExifSubIFDDirectory? = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory::class.java)

        if (gpsDirectory != null && exifDirectory != null &&
            gpsDirectory.containsTag(GpsDirectory.TAG_LATITUDE) && gpsDirectory.containsTag(GpsDirectory.TAG_LONGITUDE)
        ) {
            val pdsLat: Double = gpsDirectory.geoLocation.latitude
            val pdsLon: Double = gpsDirectory.geoLocation.longitude

            val photo = Photo(
                imagePath = path,
                latitude = pdsLat,
                longitude = pdsLon,
                timestamp = exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, TimeZone.getTimeZone("GMT+9")) ?: Date(),
                footprint = null,
            )
            photoRepo.save(photo)
        } else {
            val photo = Photo(
                imagePath = path,
                longitude = NO_META_DATA,
                latitude = NO_META_DATA,
                timestamp = Date(),
                footprint = null,
            )
            photoRepo.save(photo)
        }
    }

    @Transactional
    override fun deletePhotoFromDatabaseAndServer(photo: Photo) {
        amazonS3Client.deleteObject(bucketName, photo.imagePath)
        photoRepo.delete(photo)
    }
}
