package ru.woodymsk.socialapp.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.presentation.common.BackButtonListener
import ru.woodymsk.socialapp.presentation.profile.compose.ProfileView
import ru.woodymsk.socialapp.presentation.theme.SocialAppTheme

@AndroidEntryPoint
class ProfileScreenFragment : Fragment(), BackButtonListener {

    companion object {
        fun newInstance() = ProfileScreenFragment()
    }

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireActivity()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SocialAppTheme {
                    ProfileView(profileViewModel)
                }
            }
        }

    override fun onBackPressed() = profileViewModel.onBackPressed()
}