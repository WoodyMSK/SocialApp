package ru.woodymsk.socialapp.ui.event_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.woodymsk.socialapp.databinding.FragmentEventScreenBinding

class EventScreenFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = EventScreenFragment()
    }

    lateinit var binding: FragmentEventScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

}