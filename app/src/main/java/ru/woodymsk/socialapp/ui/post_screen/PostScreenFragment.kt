package ru.woodymsk.socialapp.ui.post_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.woodymsk.socialapp.databinding.FragmentPostScreenBinding

class PostScreenFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = PostScreenFragment()
    }

    lateinit var binding: FragmentPostScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

}