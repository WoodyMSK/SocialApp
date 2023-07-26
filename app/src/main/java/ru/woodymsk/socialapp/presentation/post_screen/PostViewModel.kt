package ru.woodymsk.socialapp.presentation.post_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.data.model.PostsItem
import ru.woodymsk.socialapp.domain.post_screen.PostRepository
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {
    private val _posts = MutableLiveData<List<PostsItem>>()
    val posts: LiveData<List<PostsItem>>
        get() = _posts

    init {
        loadAllPosts()
    }

    private fun loadAllPosts() = viewModelScope.launch {
        _posts.value = postRepository.getAllPostList()
    }
}