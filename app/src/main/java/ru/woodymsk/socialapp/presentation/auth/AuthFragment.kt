package ru.woodymsk.socialapp.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.databinding.FragmentAuthBinding
import ru.woodymsk.socialapp.domain.navigator
import ru.woodymsk.socialapp.presentation.registration.RegistrationScreenFragment

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

        binding.bAuthRegistration.setOnClickListener { onRegistrationClick() }

        return binding.root
    }

    private fun onRegistrationClick() {
        navigator().navigateTo(RegistrationScreenFragment.newInstance())
    }

}