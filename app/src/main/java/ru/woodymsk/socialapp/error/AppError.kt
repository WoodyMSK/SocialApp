package ru.woodymsk.socialapp.error

import kotlinx.coroutines.CoroutineExceptionHandler
import okio.IOException
import retrofit2.HttpException
import java.sql.SQLException

val handler = CoroutineExceptionHandler { _, exception ->
    AppError.handleError(exception)
}

sealed class AppError(var code: String) : RuntimeException() {

    companion object {
        fun handleError(e: Throwable): AppError {
            return when (e) {

                is HttpException -> httpErrorHandler(e.code())
                is SQLException -> DbError
                is IOException -> NetworkError
                is NullPointerException -> NullError
                is AppError -> e
                else -> UnknownError
            }
        }

        private fun httpErrorHandler(code: Int): AppError {
            return when (code) {
                400 -> ConfirmPasswordError
                500 -> ServerError
                else -> UnknownError
            }
        }
    }

    object NetworkError : AppError("error_network")
    object DbError : AppError("error_db")
    object UnknownError : AppError("error_unknown")
    object NullError : AppError("error_null")
    object ConfirmPasswordError : AppError("Неверный логин или пароль")
    object ServerError : AppError("Что-то пошло не так")

    data class ApiError(val errorMessage: String) : AppError(errorMessage)
}