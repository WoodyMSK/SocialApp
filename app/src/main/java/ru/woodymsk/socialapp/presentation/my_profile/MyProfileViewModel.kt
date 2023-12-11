package ru.woodymsk.socialapp.presentation.my_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.domain.my_profile.interactor.MyProfileInteractor
import ru.woodymsk.socialapp.error.handler
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val myProfileInteractor: MyProfileInteractor,
) : ViewModel() {

    fun logout() = viewModelScope.launch(handler) {
        myProfileInteractor.logout()
    }
}