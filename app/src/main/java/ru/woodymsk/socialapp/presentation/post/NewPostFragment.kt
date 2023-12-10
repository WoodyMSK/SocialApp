package ru.woodymsk.socialapp.presentation.post

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.databinding.FragmentNewPostBinding
import ru.woodymsk.socialapp.domain.focus
import ru.woodymsk.socialapp.domain.getSerializableCompat
import ru.woodymsk.socialapp.domain.load
import ru.woodymsk.socialapp.domain.navigator
import ru.woodymsk.socialapp.domain.post.model.Post
import ru.woodymsk.socialapp.presentation.common.PhotoImagePicker
import ru.woodymsk.socialapp.presentation.common.TextChangedListener
import ru.woodymsk.socialapp.presentation.common.checkPermissionResult
import ru.woodymsk.socialapp.presentation.common.getImagePermissionType
import ru.woodymsk.socialapp.presentation.common.isPermissionGranted
import ru.woodymsk.socialapp.presentation.common.requestPermission
import ru.woodymsk.socialapp.presentation.post.model.NewPostEvents.ContentDataError
import ru.woodymsk.socialapp.presentation.post.model.NewPostEvents.ErrorNewPosts
import ru.woodymsk.socialapp.presentation.post.model.NewPostEvents.GoToPostListScreen
import ru.woodymsk.socialapp.presentation.post.model.NewPostEvents.NewPostDataValid
import javax.inject.Inject

@AndroidEntryPoint
class NewPostFragment : Fragment() {

    companion object {
        private const val REQ_POST_KEY = "REQ_POST_KEY"
        private const val BUNDLE_POST_KEY = "BUNDLE_POST_KEY"
        fun newInstance(): Fragment = NewPostFragment()
    }

    private val viewModel: NewPostViewModel by viewModels()
    private val textChangedListener = TextChangedListener {
        viewModel.newPostDataChecked(binding.etNewPostMessage.text.toString())
    }
    private val pickPhotoLauncher =
        registerForActivityResult(StartActivityForResult()) {
            when (it.resultCode) {
                ImagePicker.RESULT_ERROR -> {
                    Snackbar.make(
                        binding.root,
                        ImagePicker.getError(it.data),
                        Snackbar.LENGTH_LONG,
                    ).show()
                }

                Activity.RESULT_OK -> viewModel.changePicture(it.data?.data)
            }
        }

    @Inject
    lateinit var photoImagePicker: PhotoImagePicker
    private lateinit var binding: FragmentNewPostBinding
    private var post = Post()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(REQ_POST_KEY) { _, bundle ->
            post = bundle.getSerializableCompat(BUNDLE_POST_KEY, Post::class.java)
            binding.etNewPostMessage.setText(post.content)
            post.attachment?.url?.let { binding.ivNewPostImage.load(it) }
            viewModel.changePicture(post.attachment?.url?.toUri())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val permissionsImageRequest = getImagePermissionRequest()
        val permissionsCameraRequest = getCameraPermissionRequest()

        observeNewPostEvents()

        with(binding) {
            etNewPostMessage.focus()
            etNewPostMessage.addTextChangedListener(textChangedListener)
            bNewPostPublish.setOnClickListener {
                if (post.attachment != null) {
                    viewModel.changePicture(null)
                }
                viewModel.changeContent(
                    postId = post.id,
                    content = binding.etNewPostMessage.text.toString(),
                    attachment = post.attachment
                )
                viewModel.createPost()
            }
            bNewPostCloseFragment.setOnClickListener {
                navigator().navigateTo(PostScreenFragment.newInstance())
            }
            bNewPostPickImage.setOnClickListener {
                requestPermission(permissionsImageRequest, getImagePermissionType())
            }
            bNewPostTakePhoto.setOnClickListener {
                requestPermission(permissionsCameraRequest, CAMERA)
            }
            bNewPostRemoveImage.setOnClickListener {
                viewModel.changePicture(null)
                post = post.copy(attachment = null)
            }
        }

        viewModel.postPicture.observe(viewLifecycleOwner) {
            if (it.uri == null) {
                binding.flImageContainer.visibility = View.GONE
                return@observe
            }
            binding.flImageContainer.visibility = View.VISIBLE
            binding.ivNewPostImage.setImageURI(it.uri)
        }
    }

    private fun observeNewPostEvents() {
        viewModel.newPostEvents.observe(viewLifecycleOwner) { event ->
            with(binding) {
                bNewPostPublish.isEnabled = false
                when (event) {
                    is ContentDataError -> bNewPostPublish.isEnabled = false
                    is NewPostDataValid -> bNewPostPublish.isEnabled = true
                    is GoToPostListScreen -> {
                        navigator().navigateTo(PostScreenFragment.newInstance())
                    }
                    is ErrorNewPosts -> {
                        Toast.makeText(
                            requireActivity(),
                            event.appError.code,
                            Toast.LENGTH_LONG
                        )
                        .show()
                    }
                }
            }
        }
    }

    private fun getImagePermissionRequest() =
        registerForActivityResult(RequestPermission()) {
            if (isPermissionGranted(getImagePermissionType())) {
                photoImagePicker.pickImage(requireActivity(), pickPhotoLauncher::launch)
            } else {
                checkPermissionResult(getImagePermissionType())
            }
        }

    private fun getCameraPermissionRequest() =
        registerForActivityResult(RequestPermission()) {
            if (isPermissionGranted(CAMERA)) {
                photoImagePicker.takePhoto(requireActivity(), pickPhotoLauncher::launch)
            } else {
                checkPermissionResult(CAMERA)
            }
        }
}