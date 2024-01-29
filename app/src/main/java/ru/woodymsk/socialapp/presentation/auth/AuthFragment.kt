package ru.woodymsk.socialapp.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.databinding.FragmentAuthBinding
import ru.woodymsk.socialapp.presentation.common.BackButtonListener

@AndroidEntryPoint
class AuthFragment : Fragment(), BackButtonListener {

    companion object {
        fun newInstance() = AuthFragment()
    }

    private val viewModel: AuthViewModel by viewModels()
    lateinit var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAuthBinding.inflate(inflater, container, false)

        with(binding) {
            bAuthScreenLogIn.setOnClickListener { onLoginClick() }
            bAuthRegistration.setOnClickListener { onRegistrationClick() }
        }

        return binding.root
    }

    override fun onBackPressed() = viewModel.onBackPressed()

    private fun onLoginClick() = viewModel.onLoginClick()

    private fun onRegistrationClick() = viewModel.onRegistrationClick()

}