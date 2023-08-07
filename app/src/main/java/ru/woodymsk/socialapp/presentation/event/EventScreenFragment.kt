package ru.woodymsk.socialapp.presentation.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.databinding.FragmentEventScreenBinding

@AndroidEntryPoint
class EventScreenFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = EventScreenFragment()
    }

    private val viewModel: EventViewModel by viewModels()
    lateinit var binding: FragmentEventScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventScreenBinding.inflate(inflater, container, false)
        val adapter = EventAdapter()
        binding.rvEventScreenListPost.adapter = adapter
        viewModel.events.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

}