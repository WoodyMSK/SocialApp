package ru.woodymsk.socialapp.presentation.my_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.databinding.FragmentMyProfileBinding
import ru.woodymsk.socialapp.domain.navigation.BottomNavigation
import ru.woodymsk.socialapp.presentation.common.BackButtonListener
import ru.woodymsk.socialapp.presentation.common.TabTag

@AndroidEntryPoint
class MyProfileScreenFragment : Fragment(), BackButtonListener {

    companion object {
        fun newInstance() = MyProfileScreenFragment()
    }

    private val viewModel: MyProfileViewModel by viewModels()
    lateinit var binding: FragmentMyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bMyProfileLogout.setOnClickListener { logout() }
    }

    override fun onBackPressed() = viewModel.onBackPressed()

    private fun logout() {
        viewModel.logout()
        (requireActivity() as BottomNavigation).openTabWithNavigationReset(TabTag.AUTHORIZATION)
    }
}