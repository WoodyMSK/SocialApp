package ru.woodymsk.socialapp.presentation.my_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.domain.my_profile.interactor.MyProfileInteractor
import ru.woodymsk.socialapp.error.handler
import ru.woodymsk.socialapp.presentation.navigation.Screens.authScreen
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val myProfileInteractor: MyProfileInteractor,
    private val router: Router,
) : ViewModel() {

    fun logout() = viewModelScope.launch(handler) {
        myProfileInteractor.logout()
        router.replaceScreen(authScreen())
    }
}