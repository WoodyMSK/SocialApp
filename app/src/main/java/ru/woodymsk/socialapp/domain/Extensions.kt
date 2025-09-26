package ru.woodymsk.socialapp.domain

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.data.model.ErrorResponse
import ru.woodymsk.socialapp.error.AppError
import java.io.Serializable

private const val KEY_TOKEN = "token"
private const val KEY_USER_ID = "user_id"
private const val KEY_USER_LOGIN = "user_login"
private const val KEY_USER_NAME = "user_name"
private const val KEY_USER_AVATAR = "user_avatar"

var SharedPreferences.token: String?
    get() = getString(KEY_TOKEN, null)
    set(value) {
        edit { putString(KEY_TOKEN, value) }
    }

var SharedPreferences.userId: Int
    get() = getInt(KEY_USER_ID, 0)
    set(value) {
        edit { putInt(KEY_USER_ID, value) }
    }

var SharedPreferences.userLogin: String?
    get() = getString(KEY_USER_LOGIN, null)
    set(value) {
        edit { putString(KEY_USER_LOGIN, value) }
    }

var SharedPreferences.userName: String?
    get() = getString(KEY_USER_NAME, null)
    set(value) {
        edit { putString(KEY_USER_NAME, value) }
    }

var SharedPreferences.userAvatar: String?
    get() = getString(KEY_USER_AVATAR, null)
    set(value) {
        edit { putString(KEY_USER_AVATAR, value) }
    }

fun Int?.orZero(): Int = this ?: 0

fun Boolean?.orFalse(): Boolean = this ?: false

fun <T : Serializable?> Bundle.getSerializableCompat(key: String, clazz: Class<T>): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) getSerializable(key, clazz)!! else (getSerializable(key) as T)
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
        .error(R.drawable.ic_error_24)
        .timeout(5_000)
        .transform(*transforms)
        .into(this)
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun EditText.focus() {
    text?.let { setSelection(it.length) }
    postDelayed({
        requestFocus()
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }, 200)
}

fun <T> T?.throwAppError(response: Response<T>) : T = this ?: throw AppError.ApiError(
    Gson().fromJson(
        response.errorBody()?.string(), ErrorResponse::class.java
    )
    .reason
)
// TODO вынести в core модуль
/**
 * Автоматическая подписка фрагмента на flow
 * @param state при каком состоянии lifecycle подписываться и отписываться
 * @param block функция подписки на flow с обработчиком
 */
inline fun Fragment.observeFlow(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(state) {
            block()
        }
    }
}