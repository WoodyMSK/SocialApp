package ru.woodymsk.socialapp.domain

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatDate(inputDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    // Установка временной зоны на UTC, так как исходная дата имеет 'Z' в конце.
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")

    try {
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date as Date)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ""
}

// Преобразование "230420252326" -> "2025-04-23T23:26:00Z"
fun convertDateToIsoFormat(datetime: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmm")
    val localDateTime = LocalDateTime.parse(datetime, inputFormatter)
    return localDateTime
        .atZone(ZoneId.systemDefault())
        .withZoneSameInstant(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_INSTANT)
}

// Преобразование "2025-04-23T23:26:00Z" -> "230420252326"
fun convertDateFromIsoFormat(isoDateTime: String): String {
        val zonedDateTime = ZonedDateTime.parse(isoDateTime, DateTimeFormatter.ISO_DATE_TIME)
        val formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmm")
        // Конвертируем в системную временную зону вместо UTC
        return zonedDateTime
            .withZoneSameInstant(ZoneId.systemDefault())
            .format(formatter)
}

// Преобразование "190720250000" -> "2025-07-19T00:00"
fun parseDate(datetime: String): LocalDateTime =
    LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("ddMMyyyyHHmm"))

fun isValidDate(datetime: String): Boolean {
    if (datetime.length != 12) return false
    return try {
        val day = datetime.substring(0..1).toInt()
        val month = datetime.substring(2..3).toInt()
        val year = datetime.substring(4..7).toInt()
        val hour = datetime.substring(8..9).toInt()
        val minute = datetime.substring(10..11).toInt()

        // Проверка базовых диапазонов
        if (month !in 1..12 || day !in 1..31 || hour !in 0..23 || minute !in 0..59) return false

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1) // Месяцы в Calendar начинаются с 0
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Проверка корректности даты (например, 30 февраля)
        val isDateValid = calendar.get(Calendar.YEAR) == year &&
                calendar.get(Calendar.MONTH) == month - 1 &&
                calendar.get(Calendar.DAY_OF_MONTH) == day &&
                calendar.get(Calendar.HOUR_OF_DAY) == hour &&
                calendar.get(Calendar.MINUTE) == minute

        if (!isDateValid) return false

        // Проверка, что переданная дата и время в будущем
        calendar.timeInMillis > System.currentTimeMillis()
    } catch (e: Exception) {
        false
    }
}

// Создание временного файла для uri
fun createTempImageUri(context: Context): Uri? {
    return try {
        val tempFile = File.createTempFile(
            "IMG_${System.currentTimeMillis()}",
            ".jpg",
            context.cacheDir
        )
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempFile
        )
    } catch (e: Exception) {
        null
    }
}

fun copyUriToFile(context: Context, contentUri: Uri, mimeType: String?): File? {
    return try {
        // Получаем InputStream из ContentResolver
        val inputStream: InputStream? = context.contentResolver.openInputStream(contentUri)

        // Определяем расширение файла по MIME-типу
        val extension = when {
            mimeType?.startsWith("image/") == true -> ".jpg"
            mimeType?.startsWith("video/") == true -> ".mp4"
            else -> ".tmp"
        }

        // Создаем временный файл в кэше приложения
        val outputFile = File.createTempFile(
            "MEDIA_${System.currentTimeMillis()}",
            extension,
            context.cacheDir
        )

        // Копируем данные
        inputStream?.use { input ->
            outputFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        outputFile // Возвращаем File с путем типа: /data/data/ru.woodymsk.socialapp/cache/IMG_123.jpg
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// converter function, more than 999 likes can be converted to 1k and so on
fun Int.formatNumberShort(): String {
    return when(this) {
        in 0..999 -> this.toString()
        in 1000..999_999 -> {
            val value = this / 1000.0
            val truncated = Math.floor(value * 10) / 10.0
            if (truncated % 1 == 0.0) "${truncated.toInt()}k" else "%.1fk".format(truncated)
        }
        else -> {
            val value = this / 1_000_000.0
            val truncated = Math.floor(value * 10) / 10.0
            if (truncated % 1 == 0.0) "${truncated.toInt()}kk" else "%.1fkk".format(truncated)
        }
    }
}

fun getVideoDuration(context: Context, videoUri: String): String? {
    return try {
        val retriever = MediaMetadataRetriever()

        if (videoUri.startsWith("http")) {
            // Для онлайн видео
            retriever.setDataSource(videoUri, HashMap())
        } else {
            // Для локальных файлов
            retriever.setDataSource(context, videoUri.toUri())
        }
        // Получаем длительность видео
        val durationMs = retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_DURATION
        )?.toLongOrNull() ?: 0
        // Освобождение ресурсов
        retriever.release()
        formatVideoDuration(durationMs)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun formatVideoDuration(milliseconds: Long): String {
    val seconds = (milliseconds / 1000) % 60
    val minutes = (milliseconds / (1000 * 60)) % 60
    val hours = (milliseconds / (1000 * 60 * 60)) % 24

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}