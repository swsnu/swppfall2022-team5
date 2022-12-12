package com.swpp.footprinter.domain.photo.service

import com.amazonaws.services.s3.AmazonS3Client
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifSubIFDDirectory
import com.drew.metadata.exif.GpsDirectory
import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.photo.repository.PhotoRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import com.drew.metadata.Metadata
import com.swpp.footprinter.common.NO_META_DATA
import org.springframework.beans.factory.annotation.Value
import java.util.*
import javax.transaction.Transactional

interface PhotoService {
    fun processMetadataAndSaveAsPhoto(multipartFile: MultipartFile, path: String)
    fun deletePhotoFromDatabaseAndServer(photo: Photo)
}

@Service
class PhotoServiceImpl(
    private val photoRepo: PhotoRepository,
    private val amazonS3Client: AmazonS3Client,

    @Value("\${cloud.aws.s3.bucket-name}")
    private val bucketName: String
) : PhotoService {
    @Transactional
    override fun processMetadataAndSaveAsPhoto(multipartFile: MultipartFile, path: String) {

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
