package ru.woodymsk.socialapp.presentation.start_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.databinding.FragmentStartScreenBinding
import ru.woodymsk.socialapp.domain.navigator
import ru.woodymsk.socialapp.presentation.registration_screen.RegistrationScreenFragment

@AndroidEntryPoint
class StartScreenFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = StartScreenFragment()
    }

    private lateinit var binding: FragmentStartScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartScreenBinding.inflate(inflater, container, false)

        binding.bStartScreenRegistration.setOnClickListener { onRegistrationClick() }

        return binding.root
    }

    private fun onRegistrationClick() {
        navigator().navigateTo(RegistrationScreenFragment.newInstance())
    }

}