package ru.woodymsk.socialapp.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.databinding.FragmentAuthBinding

@AndroidEntryPoint
class AuthFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = AuthFragment()
    }

    private lateinit var binding: FragmentAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)

        with(binding) {
            bAuthScreenLogIn.setOnClickListener { viewModel.onLoginClick() }
            bAuthRegistration.setOnClickListener { viewModel.onRegistrationClick() }
        }

        return binding.root
    }
}