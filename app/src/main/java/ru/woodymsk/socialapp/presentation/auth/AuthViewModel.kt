package ru.woodymsk.socialapp.presentation.auth

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.woodymsk.socialapp.domain.navigation.subnavigation.LocalCiceroneHolder
import ru.woodymsk.socialapp.domain.navigation.RouterProvider
import ru.woodymsk.socialapp.presentation.common.Screens
import ru.woodymsk.socialapp.presentation.common.TabTag.AUTHORIZATION
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val ciceroneHolder: LocalCiceroneHolder,
) : ViewModel(), RouterProvider {

    override val router: Router
        get() = ciceroneHolder.getCicerone(AUTHORIZATION).router

    fun onBackPressed() = router.exit()

    fun onLoginClick() = router.navigateTo(Screens.onLoginScreenTab())

    fun onRegistrationClick() = router.navigateTo(Screens.onRegistrationScreenTab())

}