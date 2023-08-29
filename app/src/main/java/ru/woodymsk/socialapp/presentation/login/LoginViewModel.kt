package ru.woodymsk.socialapp.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.domain.login.Interactor.LoginInteractor
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.presentation.login.model.LoginEvents
import ru.woodymsk.socialapp.presentation.login.model.LoginEvents.LoginDataError
import ru.woodymsk.socialapp.presentation.login.model.LoginEvents.PasswordDataError
import ru.woodymsk.socialapp.presentation.login.model.LoginEvents.LoginDataValid

import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginInteractor: LoginInteractor,
): ViewModel() {

    private val _loginEvents = MutableLiveData<LoginEvents>()
    val loginEvents: LiveData<LoginEvents> = _loginEvents

    private val loginResultHandler = CoroutineExceptionHandler { _, exception ->
        _loginEvents.value = LoginEvents.LoginError(AppError.handleError(exception))
    }

    fun logout() = loginInteractor.logout()

    fun login(login: String, password: String) = viewModelScope.launch(loginResultHandler) {
        loginInteractor.login(login, password)
        _loginEvents.value = LoginEvents.LoginSuccess(R.string.welcome)
    }

    fun loginDataChecked(login: String, password: String) {
        _loginEvents.value = when {
            login.isBlank() -> LoginDataError(loginError = R.string.is_blank)
            password.isBlank() -> PasswordDataError(passwordError = R.string.is_blank)
            else -> LoginDataValid
        }
    }
}