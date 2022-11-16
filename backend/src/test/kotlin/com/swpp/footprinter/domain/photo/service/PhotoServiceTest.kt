package com.swpp.footprinter.domain.photo.service

import com.drew.imaging.ImageMetadataReader
import com.drew.lang.GeoLocation
import com.drew.metadata.exif.GpsDirectory
import com.drew.metadata.Metadata
import com.drew.metadata.exif.ExifSubIFDDirectory
import com.swpp.footprinter.domain.footprint.repository.FootprintRepository
import com.swpp.footprinter.domain.photo.repository.PhotoRepository
import com.swpp.footprinter.domain.place.repository.PlaceRepository
import com.swpp.footprinter.domain.tag.repository.TagRepository
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.repository.UserRepository
import com.swpp.footprinter.global.TestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.util.*

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PhotoServiceTest @Autowired constructor(
    private val testHelper: TestHelper,
    @InjectMocks private val photoService: PhotoService,
    private val footprintRepo: FootprintRepository,
    private val placeRepo: PlaceRepository,
    private val tagRepo: TagRepository,
    private val userRepo: UserRepository,
    private val traceRepo: TraceRepository,
    private val photoRepo: PhotoRepository,
) {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun processMetadataAndSaveAsPhoto() {
        val mockedMetadata = mock(Metadata::class.java)
        val mockedGpsDirectory = mock(GpsDirectory::class.java)
        val mockedExifSubIFDDirectory = mock(ExifSubIFDDirectory::class.java)
        val mockedMultipartFile = mock(MultipartFile::class.java)
        val mockedGeoLocation = mock(GeoLocation::class.java)
        `when`(mockedMetadata.getFirstDirectoryOfType(GpsDirectory::class.java)).thenReturn(mockedGpsDirectory)
        `when`(mockedMetadata.getFirstDirectoryOfType(ExifSubIFDDirectory::class.java)).thenReturn(mockedExifSubIFDDirectory)
        `when`(mockedGpsDirectory.containsTag(GpsDirectory.TAG_LATITUDE)).thenReturn(true)
        `when`(mockedGpsDirectory.containsTag(GpsDirectory.TAG_LONGITUDE)).thenReturn(true)
        `when`(mockedGpsDirectory.geoLocation).thenReturn(mockedGeoLocation)
        `when`(mockedGeoLocation.latitude).thenReturn(127.0)
        `when`(mockedGeoLocation.longitude).thenReturn(36.0)
        `when`(mockedExifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, TimeZone.getTimeZone("GMT+9"))).thenReturn(Date())
        `when`(mockedMultipartFile.inputStream).thenReturn(InputStream.nullInputStream())

        mockStatic(ImageMetadataReader::class.java).use { mockedMetadataReader ->
            mockedMetadataReader.`when`<Any> { ImageMetadataReader.readMetadata(any(InputStream::class.java)) }.thenReturn(mockedMetadata)
            photoService.processMetadataAndSaveAsPhoto(mockedMultipartFile, "testPath")

            val createdPhoto = photoRepo.findByIdOrNull(1)!!
            assertThat(createdPhoto).extracting("imagePath").isEqualTo("testPath")
            assertThat(createdPhoto).extracting("latitude").isEqualTo(127.0)
            assertThat(createdPhoto).extracting("longitude").isEqualTo(36.0)
        }
    }

    @Test
    fun deletePhotoFromDatabaseAndServer() {
    }
}
