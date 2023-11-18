package ru.woodymsk.socialapp.presentation.post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.domain.post.interactor.PostInteractor
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.presentation.post.model.PostsEvent
import ru.woodymsk.socialapp.presentation.post.model.PostsEvent.ErrorAuth
import ru.woodymsk.socialapp.presentation.post.model.PostsEvent.ErrorPosts
import ru.woodymsk.socialapp.presentation.post.model.PostsEvent.ShowPosts
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postInteractor: PostInteractor,
    private val auth: AppAuth,
) : ViewModel() {

    private val _posts = MutableLiveData<PostsEvent>()
    val posts: Flow<PostsEvent> = _posts.asFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    init {
        loadPagedPost()
    }

    fun onLikeButtonClick(postId: Int, likedByMe: Boolean) =
        viewModelScope.launch(exceptionHandler) {
            if (checkAuth()) {
                postInteractor.onLikeButtonClick(postId, likedByMe)
            }
        }

    fun checkAuth(): Boolean {
        return if (auth.authStateFlow.value.id == 0) {
            _posts.postValue(ErrorAuth(R.string.registration_require))
            false
        } else {
            true
        }
    }

    private fun loadPagedPost() =
        viewModelScope.launch(exceptionHandler) {
            postInteractor.getPagedPostList()
                .cachedIn(viewModelScope)
                .distinctUntilChanged()
                .collectLatest {
                    _posts.postValue(ShowPosts(it))
                }
        }

    private fun handleError(e: Throwable) =
        _posts.postValue(ErrorPosts(AppError.handleError(e)))
}