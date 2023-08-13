package ru.woodymsk.socialapp.domain

import android.content.SharedPreferences
import android.widget.ImageView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import ru.woodymsk.socialapp.R

private const val KEY = "token"

var SharedPreferences.token: String?
    get() = getString(KEY, null)
    set(value) {
        edit { putString(KEY, value) }
    }

fun Int?.orZero(): Int = this ?: 0

fun Boolean?.orFalse(): Boolean = this ?: false

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
        .error(R.drawable.ic_error_24)
        .timeout(5_000)
        .transform(*transforms)
        .into(this)
}

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}