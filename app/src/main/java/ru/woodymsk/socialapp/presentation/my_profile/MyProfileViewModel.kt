package ru.woodymsk.socialapp.presentation.my_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.domain.my_profile.interactor.MyProfileInteractor
import ru.woodymsk.socialapp.domain.navigation.subnavigation.LocalCiceroneHolder
import ru.woodymsk.socialapp.error.handler
import ru.woodymsk.socialapp.domain.navigation.RouterProvider
import ru.woodymsk.socialapp.presentation.common.TabTag.MY_PROFILE
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val myProfileInteractor: MyProfileInteractor,
    private val ciceroneHolder: LocalCiceroneHolder,
) : ViewModel(), RouterProvider {

    override val router: Router
        get() = ciceroneHolder.getCicerone(MY_PROFILE).router

    fun logout() = viewModelScope.launch(handler) {
        myProfileInteractor.logout()
    }

    fun onBackPressed() = router.exit()

}