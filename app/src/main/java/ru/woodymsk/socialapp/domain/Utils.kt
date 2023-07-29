package ru.woodymsk.socialapp.domain

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import ru.woodymsk.socialapp.R.drawable.ic_error_24
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

fun ImageView.load(
    url: String,
    vararg transforms: BitmapTransformation = emptyArray()
) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    Glide.with(this)
        .load(url)
        .placeholder(circularProgressDrawable)
        .error(ic_error_24)
        .timeout(5_000)
        .transform(*transforms)
        .into(this)
}

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}