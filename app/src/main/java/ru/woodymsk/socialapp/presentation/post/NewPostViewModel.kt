package ru.woodymsk.socialapp.presentation.post

import android.net.Uri
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.data.model.Attachment
import ru.woodymsk.socialapp.data.model.MediaUpload
import ru.woodymsk.socialapp.domain.post.interactor.PostInteractor
import ru.woodymsk.socialapp.domain.post.model.Post
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.presentation.navigation.Screens.postScreen
import ru.woodymsk.socialapp.presentation.post.model.NewPostEvents
import ru.woodymsk.socialapp.presentation.post.model.PictureModel
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val postInteractor: PostInteractor,
    private val router: Router,
) : ViewModel() {

    private val noPicture = PictureModel()
    private val _postPicture = MutableLiveData(noPicture)
    val postPicture: LiveData<PictureModel> = _postPicture

    private val _newPostEvents = MutableLiveData<NewPostEvents>()
    val newPostEvents: LiveData<NewPostEvents> = _newPostEvents

    private val emptyPost = Post()
    private val postContent = MutableLiveData(emptyPost)

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    fun createPost() =
        viewModelScope.launch(exceptionHandler) {
            postContent.value?.let {
                when (_postPicture.value) {
                    noPicture -> postInteractor.createPost(it, null)
                    else -> _postPicture.value?.uri?.let { uri ->
                        postInteractor.createPost(it, MediaUpload(uri.toFile()))
                    }
                }
                _newPostEvents.postValue(NewPostEvents.GoToPostListScreen)
            }
            postContent.value = emptyPost
            _postPicture.value = noPicture
        }

    fun changeContent(postId: Int, content: String, attachment: Attachment?) {
        postContent.value = postContent.value?.copy(id = postId, content = content.trim(), attachment = attachment)
    }

    fun changePicture(uri: Uri?) {
        _postPicture.value = PictureModel(uri)
    }

    fun newPostDataChecked(content: String) {
        _newPostEvents.value = when {
            content.isBlank() -> NewPostEvents.ContentDataError
            else -> NewPostEvents.NewPostDataValid
        }
    }

    fun goToPostScreen() = router.replaceScreen(postScreen())

    private fun handleError(e: Throwable) =
        _newPostEvents.postValue(NewPostEvents.ErrorNewPosts(AppError.handleError(e)))
}