package ru.woodymsk.socialapp.presentation.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.woodymsk.socialapp.databinding.FragmentPostScreenBinding
import ru.woodymsk.socialapp.domain.observeFlow
import ru.woodymsk.socialapp.presentation.post.PostsEvent.ErrorPosts
import ru.woodymsk.socialapp.presentation.post.PostsEvent.ShowPosts

@AndroidEntryPoint
class PostScreenFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = PostScreenFragment()
    }

    private val viewModel: PostViewModel by viewModels()
    lateinit var binding: FragmentPostScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostScreenBinding.inflate(inflater, container, false)
        val adapter = PostAdapter()
        binding.rvPostScreenListPost.adapter = adapter

        observeFlow {
            viewModel.posts.collectLatest { event ->
                when (event) {
                    is ShowPosts -> adapter.submitData(event.events)
                    is ErrorPosts -> {
                        Toast.makeText(
                            requireActivity(),
                            event.appError.code,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        return binding.root
    }
}