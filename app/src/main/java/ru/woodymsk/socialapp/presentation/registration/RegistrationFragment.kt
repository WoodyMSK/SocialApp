package ru.woodymsk.socialapp.presentation.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.databinding.FragmentRegistrationBinding

@AndroidEntryPoint
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