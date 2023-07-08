package ru.woodymsk.socialapp.ui.registration_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.woodymsk.socialapp.databinding.FragmentRegistrationBinding

class RegistrationScreenFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = RegistrationScreenFragment()
    }

    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        return binding.root
    }

}