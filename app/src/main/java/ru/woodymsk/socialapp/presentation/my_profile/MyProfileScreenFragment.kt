package ru.woodymsk.socialapp.presentation.my_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.databinding.FragmentMyProfileBinding
import ru.woodymsk.socialapp.domain.navigator
import ru.woodymsk.socialapp.presentation.auth.AuthFragment

@AndroidEntryPoint
class MyProfileScreenFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = MyProfileScreenFragment()
    }

    private val viewModel: MyProfileViewModel by viewModels()
    private lateinit var binding: FragmentMyProfileBinding

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

    private fun logout() {
        viewModel.logout()
        navigator().navigateTo(AuthFragment.newInstance())
    }
}