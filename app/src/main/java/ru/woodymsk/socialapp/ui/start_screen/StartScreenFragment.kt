package ru.woodymsk.socialapp.ui.start_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.woodymsk.socialapp.databinding.FragmentStartScreenBinding
import ru.woodymsk.socialapp.ui.registration_screen.RegistrationScreenFragment
import ru.woodymsk.socialapp.ui.navigation.navigator

class StartScreenFragment : Fragment() {

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