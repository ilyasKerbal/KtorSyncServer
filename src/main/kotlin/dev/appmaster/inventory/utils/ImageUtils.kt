package dev.appmaster.inventory.utils

import io.ktor.util.*

private val allowedExtensions = arrayOf<String>(".jpeg", ".jpg", ".png", ".webp")
private val jpegSignature = "FFD8FF"
private val pngSignature = "89504E470D0A1A0A"
private val webpRIFF = "52494646"
private val webpWEBP = "57454250"

fun isImageExtValid(fileName: String?): Boolean {
    if (fileName == null) return true
    return allowedExtensions.any { fileName.endsWith(it, ignoreCase = true) }
}

fun isValidImage(bytes: ByteArray?): Boolean {
    if (bytes == null) return true
    val sizeCondition = bytes.size > 13
    return sizeCondition && (isJpeg(bytes) || isPng(bytes) || isWebP(bytes))
}

fun getFileExtension(fileName: String): String? {
    val matchResult = Regex(".*\\.(.*)$").find(fileName)
    return matchResult?.groups?.get(1)?.value
}

private fun isJpeg(bytes: ByteArray) = hex(bytes.copyOfRange(0, 3)).uppercase() == jpegSignature
private fun isPng(bytes: ByteArray) = hex(bytes.copyOfRange(0, 8)).uppercase() == pngSignature
private fun isWebP(bytes: ByteArray) = hex(bytes.copyOfRange(0, 4)).uppercase() == webpRIFF && hex(bytes.copyOfRange(8, 12)).uppercase() == webpWEBP
