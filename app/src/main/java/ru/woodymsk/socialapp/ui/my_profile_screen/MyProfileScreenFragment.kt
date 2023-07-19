package ru.woodymsk.socialapp.ui.registration_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.woodymsk.socialapp.databinding.FragmentMyProfileBinding

class MyProfileScreenFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = MyProfileScreenFragment()
    }

    private lateinit var binding: FragmentMyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

}