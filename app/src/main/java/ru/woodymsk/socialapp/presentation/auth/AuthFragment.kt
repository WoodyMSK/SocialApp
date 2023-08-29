package ru.woodymsk.socialapp.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.databinding.FragmentAuthBinding
import ru.woodymsk.socialapp.domain.navigator
import ru.woodymsk.socialapp.presentation.login.LoginFragment
import ru.woodymsk.socialapp.presentation.registration.RegistrationFragment

@AndroidEntryPoint
class AuthFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = AuthFragment()
    }

    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)

        with(binding) {
            bAuthScreenLogIn.setOnClickListener { onLoginClick() }
            bAuthRegistration.setOnClickListener { onRegistrationClick() }
        }

        return binding.root
    }

    private fun onLoginClick() {
        navigator().navigateTo(LoginFragment.newInstance())
    }

    private fun onRegistrationClick() {
        navigator().navigateTo(RegistrationFragment.newInstance())
    }
}