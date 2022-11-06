package com.swpp.footprinter.domain.photo.service

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifSubIFDDirectory
import com.drew.metadata.exif.GpsDirectory
import com.swpp.footprinter.common.exception.ErrorType
import com.swpp.footprinter.common.exception.FootprinterException
import com.swpp.footprinter.domain.photo.model.Photo
import com.swpp.footprinter.domain.photo.repository.PhotoRepository
import com.swpp.footprinter.utils.dateToString8601
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import com.drew.metadata.Metadata

interface PhotoService {
    fun processMetadataAndSaveAsPhoto(multipartFile: MultipartFile, path: String): Photo
}

@Service
class PhotoServiceImpl(
    private val photoRepository: PhotoRepository,
): PhotoService {
    override fun processMetadataAndSaveAsPhoto(multipartFile: MultipartFile, path: String): Photo {
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
            file.delete()
            return Photo("", "", "", "", null)
        }
    }
}
