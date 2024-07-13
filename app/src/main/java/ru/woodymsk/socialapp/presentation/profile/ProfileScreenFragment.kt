package ru.woodymsk.socialapp.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import ru.woodymsk.socialapp.presentation.common.BackButtonListener
import ru.woodymsk.socialapp.presentation.common.ViewModelFactory
import ru.woodymsk.socialapp.presentation.profile.compose.ProfileView
import ru.woodymsk.socialapp.presentation.theme.SocialAppTheme
import javax.inject.Inject

class ProfileScreenFragment : Fragment(), BackButtonListener {

    companion object {
        fun newInstance() = ProfileScreenFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ProfileViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireActivity()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SocialAppTheme {
                    ProfileView(viewModel)
                }
            }
        }

    override fun onBackPressed() = viewModel.onBackPressed()
}