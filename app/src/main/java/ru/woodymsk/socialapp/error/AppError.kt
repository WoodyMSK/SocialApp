package ru.woodymsk.socialapp.error

import okio.IOException
import java.sql.SQLException

sealed class AppError(var code: String) : RuntimeException() {

    companion object {
        fun from(e: Throwable): AppError = when (e) {
            is AppError -> e
            is SQLException -> DbError
            is IOException -> NetworkError
            else -> UnknownError
        }
    }

    object NetworkError : AppError("error_network")
    object DbError : AppError("error_db")
    object UnknownError : AppError("error_unknown")

    data class ApiError(val errorMessage: String) : AppError(errorMessage)
}