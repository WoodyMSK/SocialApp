package ru.woodymsk.socialapp.presentation.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.domain.post.interactor.PostInteractor
import ru.woodymsk.socialapp.domain.post.model.Post
import ru.woodymsk.socialapp.error.handler
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postInteractor: PostInteractor,
) : ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts

    init {
        loadAllPosts()
    }

    private fun loadAllPosts() = viewModelScope.launch(handler) {
        _posts.value = postInteractor.getAllPostList()
    }
}