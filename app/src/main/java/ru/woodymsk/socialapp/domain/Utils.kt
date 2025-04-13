package ru.woodymsk.socialapp.domain

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
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

fun parseDate(datetime: String): LocalDateTime? {
    if (datetime.length != 12) return null
    return try {
        LocalDateTime.of(
            datetime.substring(4..7).toInt(),   // Год
            datetime.substring(2..3).toInt(),    // Месяц
            datetime.substring(0..1).toInt(),    // День
            datetime.substring(8..9).toInt(),   // Часы
            datetime.substring(10..11).toInt(), // Минуты
        )
    } catch (e: Exception) {
        null
    }
}

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