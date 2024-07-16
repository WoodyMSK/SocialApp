package ru.woodymsk.socialapp.presentation.post

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.collectLatest
import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.databinding.FragmentPostScreenBinding
import ru.woodymsk.socialapp.domain.observeFlow
import ru.woodymsk.socialapp.domain.post.model.Post
import ru.woodymsk.socialapp.presentation.common.BackButtonListener
import ru.woodymsk.socialapp.presentation.common.PagingLoadStateAdapter
import ru.woodymsk.socialapp.presentation.common.ViewModelFactory
import ru.woodymsk.socialapp.presentation.post.adapter.PostAdapter
import ru.woodymsk.socialapp.presentation.post.adapter.PostClickListener
import ru.woodymsk.socialapp.presentation.post.model.PostsEvent.ErrorAuth
import ru.woodymsk.socialapp.presentation.post.model.PostsEvent.ErrorPosts
import ru.woodymsk.socialapp.presentation.post.model.PostsEvent.ShowPosts
import javax.inject.Inject

class PostScreenFragment : Fragment(), BackButtonListener {

    companion object {
        private const val BUNDLE_POST_KEY = "BUNDLE_POST_KEY"

        fun newInstance() = PostScreenFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: PostViewModel

    @Inject
    lateinit var auth: AppAuth
    private lateinit var binding: FragmentPostScreenBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[PostViewModel::class.java]
    }

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
                    val bundle = Bundle()
                    bundle.putSerializable(BUNDLE_POST_KEY, post)
                    onNewPostClick(bundle)
                }
                override fun onDelete(id: Int) {
                    viewModel.onDeleteButtonClick(id.toString())
                }
            }
        )

        binding.apply {
            rvPostScreenListPost.adapter =
                adapter.withLoadStateFooter(footer = PagingLoadStateAdapter(adapter::retry))
            swipeRefreshPostScreen.setOnRefreshListener {
                viewModel.loadPagedPost()
            }
        }

        observeFlow {
            viewModel.posts.collectLatest { event ->
                when (event) {
                    is ShowPosts -> {
                        binding.swipeRefreshPostScreen.isRefreshing = false
                        adapter.submitData(event.events)
                    }
                    is ErrorPosts -> {
                        binding.swipeRefreshPostScreen.isRefreshing = false
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
            if (viewModel.isAuth()) run {
                onNewPostClick(null)
            }
        }
    }

    override fun onBackPressed() = viewModel.onBackPressed()

    private fun onNewPostClick(bundle: Bundle?) = viewModel.onNewPostClick(bundle)

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
                DialogInterface.BUTTON_POSITIVE -> viewModel.goToAuthScreen()
            }
        }
    }
}