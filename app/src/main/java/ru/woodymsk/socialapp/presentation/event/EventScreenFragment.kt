package ru.woodymsk.socialapp.presentation.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.databinding.FragmentEventScreenBinding
import ru.woodymsk.socialapp.domain.observeFlow
import ru.woodymsk.socialapp.presentation.common.BackButtonListener
import ru.woodymsk.socialapp.presentation.event.adapter.EventAdapter
import ru.woodymsk.socialapp.presentation.event.model.EventsEvent.ShowEvents

@AndroidEntryPoint
class EventScreenFragment : Fragment(), BackButtonListener {

    companion object {
        fun newInstance() = EventScreenFragment()
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

        observeFlow {
            viewModel.events.collect { event ->
                when (event) {
                    is ShowEvents -> adapter.submitList(event.events)
                }
            }
        }

        return binding.root
    }

    override fun onBackPressed() = viewModel.onBackPressed()

}