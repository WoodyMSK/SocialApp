package ru.woodymsk.socialapp.presentation.auth

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.woodymsk.socialapp.domain.navigation.RouterProvider
import ru.woodymsk.socialapp.presentation.common.Screens
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    override val router: Router
) : ViewModel(), RouterProvider {

    fun onBackPressed() = router.exit()

    fun onLoginClick() = router.navigateTo(Screens.loginScreen())

    fun onRegistrationClick() = router.navigateTo(Screens.loginScreen())

}