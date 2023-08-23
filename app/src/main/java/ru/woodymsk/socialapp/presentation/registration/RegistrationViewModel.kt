package ru.woodymsk.socialapp.presentation.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.domain.registration.Interactor.RegistrationInteractor
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationFormState
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationFormState.NameError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationFormState.LoginError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationFormState.PasswordError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationFormState.IsRegistrationDataValid
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationFormState.ConfirmPasswordError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationResult
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationInteractor: RegistrationInteractor,
) : ViewModel() {

    private val _registrationFormState = MutableLiveData<RegistrationFormState>()
    val registrationFormState: LiveData<RegistrationFormState> = _registrationFormState

    private val _registrationResult = MutableLiveData<RegistrationResult>()
    val registrationResult: LiveData<RegistrationResult> = _registrationResult

    private val registrationResultHandler = CoroutineExceptionHandler { _, _ ->
        _registrationResult.value = RegistrationResult.Error(error = R.string.registration_failed)
    }

    companion object {
        const val MIN_STRING_LENGTH = 3
    }

    fun registration(login: String, password: String, userName: String) =
        viewModelScope.launch(registrationResultHandler) {
            registrationInteractor.registration(login, password, userName)
            _registrationResult.value = RegistrationResult.Success(userName = userName)
        }

    fun registrationDataChecked(
        login: String,
        password: String,
        confirmPassword: String,
        userName: String,
    ) {
        when {
            !isLengthValid(login) -> {
                _registrationFormState.value =
                    LoginError(loginError = R.string.invalid_length)
            }

            !isLengthValid(password) -> {
                _registrationFormState.value =
                    PasswordError(passwordError = R.string.invalid_length)
            }

            !isPasswordValid(password, confirmPassword) -> {
                _registrationFormState.value =
                    ConfirmPasswordError(confirmPasswordError = R.string.invalid_confirm_password)
            }

            userName.isBlank() -> {
                _registrationFormState.value = NameError(nameError = R.string.is_blank)
            }

            else -> {
                _registrationFormState.value =
                    IsRegistrationDataValid(isRegistrationDataValid = true)
            }
        }
    }

    private fun isLengthValid(string: String): Boolean = string.length >= MIN_STRING_LENGTH

    private fun isPasswordValid(password: String, confirmPassword: String): Boolean =
        password == confirmPassword

}