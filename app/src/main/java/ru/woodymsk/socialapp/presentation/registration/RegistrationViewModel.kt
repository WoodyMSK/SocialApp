package ru.woodymsk.socialapp.presentation.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.domain.navigation.RouterProvider
import ru.woodymsk.socialapp.domain.registration.interactor.RegistrationInteractor
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.presentation.common.Screens.authScreen
import ru.woodymsk.socialapp.presentation.common.Screens.profileScreen
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationEvents
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationEvents.ConfirmPasswordError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationEvents.LoginDataError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationEvents.NameDataError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationEvents.RegistrationDataValid
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationEvents.RegistrationError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationEvents.RegistrationSuccess
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationInteractor: RegistrationInteractor,
    override val router: Router,
) : ViewModel(), RouterProvider {

    private val _registrationEvents = MutableLiveData<RegistrationEvents>()
    val registrationEvents: LiveData<RegistrationEvents> = _registrationEvents

    private val registrationResultHandler = CoroutineExceptionHandler { _, exeption ->
        _registrationEvents.value = RegistrationError(AppError.handleError(exeption))
    }

    companion object {
        const val MIN_STRING_LENGTH = 3
    }

    fun registration(login: String, password: String, userName: String) =
        viewModelScope.launch(registrationResultHandler) {
            registrationInteractor.registration(login, password, userName)
            _registrationEvents.value = RegistrationSuccess(userName = userName)
        }

    fun registrationDataChecked(
        login: String,
        password: String,
        confirmPassword: String,
        userName: String,
    ) {
        _registrationEvents.value = when {
            !isLengthValid(login) -> LoginDataError(loginError = R.string.invalid_length)

            !isLengthValid(password) -> {
                RegistrationEvents.PasswordDataError(passwordError = R.string.invalid_length)
            }

            !isPasswordValid(password, confirmPassword) -> {
                ConfirmPasswordError(confirmPasswordError = R.string.invalid_confirm_password)
            }

            userName.isBlank() -> NameDataError(nameError = R.string.is_blank)

            else -> RegistrationDataValid
        }
    }

    fun onBackPressed() = router.backTo(authScreen())

    fun goToProfile() = router.replaceScreen(profileScreen())

    private fun isLengthValid(string: String): Boolean = string.length >= MIN_STRING_LENGTH

    private fun isPasswordValid(password: String, confirmPassword: String): Boolean =
        password == confirmPassword

}