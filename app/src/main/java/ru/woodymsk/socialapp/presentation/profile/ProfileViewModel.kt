package ru.woodymsk.socialapp.presentation.profile

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.domain.profile.interactor.ProfileInteractor
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.presentation.common.Screens
import ru.woodymsk.socialapp.presentation.common.Screens.authScreen
import ru.woodymsk.socialapp.presentation.profile.model.ProfileEvents
import ru.woodymsk.socialapp.presentation.profile.model.ProfileEvents.ProfileError
import ru.woodymsk.socialapp.presentation.profile.model.ProfileEvents.ShowProfile
import ru.woodymsk.socialapp.presentation.profile.model.ProfileEvents.LoadingState
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val router: Router,
    private val auth: AppAuth,
) : ViewModel() {

    private val _profile: MutableStateFlow<ProfileEvents> =
        MutableStateFlow(LoadingState)
    val profile: StateFlow<ProfileEvents>
        get() = _profile

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    init {
        getProfile()
    }

    fun logout() = viewModelScope.launch(exceptionHandler) {
        profileInteractor.logout()
        router.replaceScreen(authScreen())
    }

    fun onLikeButtonClick(postId: Int, likedByMe: Boolean) =
        viewModelScope.launch(exceptionHandler) {
            if (isAuth()) {
                profileInteractor.likePost(postId, likedByMe)
            }
        }

    fun onDeleteButtonClick(id: String) = viewModelScope.launch(exceptionHandler) {
        profileInteractor.deletePost(id)
    }

    fun onNewPostClick(args: Bundle) {
        router.navigateTo(Screens.newPostScreen(args))
    }

    fun onBackPressed() = router.exit()

    private fun isAuth(): Boolean = auth.authStateFlow.value.id != 0

    private fun getProfile() =
        viewModelScope.launch(exceptionHandler) {
            val profileDataRequest = async { profileInteractor.getMyProfileData() }
            val profilePostListRequest = async { profileInteractor.getMyPostList() }
            val profileData = profileDataRequest.await()
            val profilePostList = profilePostListRequest.await()
            _profile.tryEmit(ShowProfile(profileData, profilePostList))
        }

    private fun handleError(e: Throwable) = _profile.tryEmit(ProfileError(AppError.handleError(e)))
}