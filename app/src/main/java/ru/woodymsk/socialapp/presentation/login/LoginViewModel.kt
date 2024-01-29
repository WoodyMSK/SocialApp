package ru.woodymsk.socialapp.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.domain.login.interactor.LoginInteractor
import ru.woodymsk.socialapp.domain.navigation.RouterProvider
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.presentation.common.Screens.authScreen
import ru.woodymsk.socialapp.presentation.common.Screens.profileScreen
import ru.woodymsk.socialapp.presentation.login.model.LoginEvents
import ru.woodymsk.socialapp.presentation.login.model.LoginEvents.LoginDataError
import ru.woodymsk.socialapp.presentation.login.model.LoginEvents.LoginDataValid
import ru.woodymsk.socialapp.presentation.login.model.LoginEvents.LoginError
import ru.woodymsk.socialapp.presentation.login.model.LoginEvents.LoginSuccess
import ru.woodymsk.socialapp.presentation.login.model.LoginEvents.PasswordDataError
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginInteractor: LoginInteractor,
    override val router: Router,
) : ViewModel(), RouterProvider {

    private val _loginEvents = MutableLiveData<LoginEvents>()
    val loginEvents: LiveData<LoginEvents> = _loginEvents

    private val loginResultHandler = CoroutineExceptionHandler { _, exception ->
        _loginEvents.value = LoginError(AppError.handleError(exception))
    }

    fun login(login: String, password: String) = viewModelScope.launch(loginResultHandler) {
        loginInteractor.login(login, password)
        _loginEvents.value = LoginSuccess(R.string.welcome)
    }

    fun loginDataChecked(login: String, password: String) {
        _loginEvents.value = when {
            login.isBlank() -> LoginDataError(loginError = R.string.is_blank)
            password.isBlank() -> PasswordDataError(passwordError = R.string.is_blank)
            else -> LoginDataValid
        }
    }

    fun goToProfileScreen() = router.replaceScreen(profileScreen())

    fun onBackPressed() = router.backTo(authScreen())
}