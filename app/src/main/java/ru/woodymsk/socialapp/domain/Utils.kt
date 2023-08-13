package ru.woodymsk.socialapp.domain

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun parseAndFormatDate(inputDate: String): String {
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