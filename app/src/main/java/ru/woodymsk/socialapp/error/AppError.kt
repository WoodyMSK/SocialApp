package ru.woodymsk.socialapp.error

import kotlinx.coroutines.CoroutineExceptionHandler
import okio.IOException
import java.lang.NullPointerException
import java.sql.SQLException

val handler = CoroutineExceptionHandler { _, exception ->
    AppError.from(exception)
}

sealed class AppError(var code: String) : RuntimeException() {

    companion object {
        fun from(e: Throwable): AppError = when (e) {
            is AppError -> e
            is SQLException -> DbError
            is IOException -> NetworkError
            is NullPointerException -> NullError
            else -> UnknownError
        }
    }

    object NetworkError : AppError("error_network")
    object DbError : AppError("error_db")
    object UnknownError : AppError("error_unknown")
    object NullError : AppError("error_null")

    data class ApiError(val errorMessage: String) : AppError(errorMessage)
}