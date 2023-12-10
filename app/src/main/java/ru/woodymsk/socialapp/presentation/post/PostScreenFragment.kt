package ru.woodymsk.socialapp.presentation.post

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.databinding.FragmentPostScreenBinding
import ru.woodymsk.socialapp.domain.navigator
import ru.woodymsk.socialapp.domain.observeFlow
import ru.woodymsk.socialapp.domain.post.model.Post
import ru.woodymsk.socialapp.presentation.auth.AuthFragment
import ru.woodymsk.socialapp.presentation.common.PagingLoadStateAdapter
import ru.woodymsk.socialapp.presentation.post.model.PostsEvent.ErrorAuth
import ru.woodymsk.socialapp.presentation.post.model.PostsEvent.ErrorPosts
import ru.woodymsk.socialapp.presentation.post.model.PostsEvent.ShowPosts
import javax.inject.Inject

@AndroidEntryPoint
class PostScreenFragment : Fragment() {

    companion object {
        private const val REQ_POST_KEY = "REQ_POST_KEY"
        private const val BUNDLE_POST_KEY = "BUNDLE_POST_KEY"
        fun newInstance(): Fragment = PostScreenFragment()
    }

    private val viewModel: PostViewModel by viewModels()
    lateinit var binding: FragmentPostScreenBinding
    @Inject
    lateinit var auth: AppAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostScreenBinding.inflate(inflater, container, false)
        val adapter = PostAdapter(
            object : PostClickListener {
                override fun onLike(postId: Int, likedByMe: Boolean) {
                    viewModel.onLikeButtonClick(postId = postId, likedByMe = likedByMe)
                }
                override fun onEdit(post: Post) {
                    setFragmentResult(REQ_POST_KEY, bundleOf(BUNDLE_POST_KEY to post))
                    navigator().navigateTo(NewPostFragment.newInstance())
                }
            }
        )

        binding.rvPostScreenListPost.adapter =
            adapter.withLoadStateFooter(footer = PagingLoadStateAdapter(adapter::retry))

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
                    is ErrorAuth -> showLoginDialogFragment()
                }
            }
        }

        setupLoginDialogFragmentListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bPostScreenAddNewPost.setOnClickListener {
            if (viewModel.checkAuth()) {
                navigator().navigateTo(NewPostFragment.newInstance())
            }
        }
    }

    private fun showLoginDialogFragment() {
        val dialogFragment = LoginDialogFragment()
        dialogFragment.show(parentFragmentManager, LoginDialogFragment.TAG)
    }

    private fun setupLoginDialogFragmentListener() {
        parentFragmentManager.setFragmentResultListener(
            LoginDialogFragment.REQUEST_KEY,
            this
        ) { _, result ->
            when (result.getInt(LoginDialogFragment.KEY_RESPONSE)) {
                DialogInterface.BUTTON_POSITIVE -> navigator().navigateTo(AuthFragment.newInstance())
            }
        }
    }
}