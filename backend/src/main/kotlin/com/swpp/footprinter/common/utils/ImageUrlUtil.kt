package com.swpp.footprinter.common.utils

import com.amazonaws.services.s3.AmazonS3Client
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class ImageUrlUtil(
    private val amazonS3Client: AmazonS3Client,
    @Value("\${cloud.aws.s3.bucket-name}") private val bucketName: String,
) {
    fun getImageURLfromImagePath(imagePath: String): String =
        amazonS3Client.generatePresignedUrl(
            bucketName,
            imagePath,
            Calendar.getInstance().let { // Set expiration day to 1 day
                it.time = Date()
                it.add(Calendar.DATE, 1)
                it.time
            },
        ).toString()
}
