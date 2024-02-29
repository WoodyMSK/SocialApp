package ru.woodymsk.socialapp.presentation.auth

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.woodymsk.socialapp.presentation.navigation.Screens.loginScreen
import ru.woodymsk.socialapp.presentation.navigation.Screens.registrationScreen
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val router: Router,
): ViewModel() {

    fun onLoginClick() {
        router.navigateTo(loginScreen())
    }

    fun onRegistrationClick() {
        router.navigateTo(registrationScreen())
    }
}