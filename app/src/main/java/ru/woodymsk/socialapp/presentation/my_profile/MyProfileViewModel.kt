package ru.woodymsk.socialapp.presentation.my_profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.woodymsk.socialapp.domain.my_profile.interactor.MyProfileInteractor
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val myProfileInteractor: MyProfileInteractor,
) : ViewModel() {

    fun logout() = myProfileInteractor.logout()
}