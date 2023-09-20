package ru.woodymsk.socialapp.presentation.post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.woodymsk.socialapp.data.model.ErrorResponse
import ru.woodymsk.socialapp.domain.post.interactor.PostInteractor
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.presentation.post.PostsEvent.ErrorPosts
import ru.woodymsk.socialapp.presentation.post.PostsEvent.ShowPosts
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postInteractor: PostInteractor,
    private val gson: Gson,
) : ViewModel() {

    private val _posts = MutableLiveData<PostsEvent>()
    val posts: Flow<PostsEvent> = _posts.asFlow()

    private val postsReceiveHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    init {
        loadPagedPost()
    }

    private fun loadPagedPost() =
        viewModelScope.launch(postsReceiveHandler) {
            postInteractor.getPagedPostList().collect {
                _posts.postValue(ShowPosts(it))
            }
        }

    fun handleError(e: Throwable) {
        _posts.postValue(
            ErrorPosts(
                if (e is HttpException) {
                    AppError.ApiError(
                        gson.fromJson(
                            e.response()?.errorBody()?.string(),
                            ErrorResponse::class.java
                        )
                            .reason
                    )
                } else {
                    AppError.handleError(e)
                }
            )
        )
    }
}