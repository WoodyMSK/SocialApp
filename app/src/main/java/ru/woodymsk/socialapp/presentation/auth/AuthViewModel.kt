package ru.woodymsk.socialapp.presentation.auth

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import ru.woodymsk.socialapp.presentation.common.Screens
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val router: Router
) : ViewModel() {

    fun onBackPressed() = router.exit()

    fun onLoginClick() = router.navigateTo(Screens.loginScreen())

    fun onRegistrationClick() = router.navigateTo(Screens.registrationScreen())

}