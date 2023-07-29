package ru.woodymsk.socialapp.presentation.post_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.databinding.FragmentPostScreenBinding

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
        viewModel.posts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

}