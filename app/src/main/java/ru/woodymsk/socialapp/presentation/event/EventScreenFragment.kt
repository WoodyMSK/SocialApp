package ru.woodymsk.socialapp.presentation.event

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import ru.woodymsk.socialapp.databinding.FragmentEventScreenBinding
import ru.woodymsk.socialapp.domain.observeFlow
import ru.woodymsk.socialapp.presentation.common.BackButtonListener
import ru.woodymsk.socialapp.presentation.common.ViewModelFactory
import ru.woodymsk.socialapp.presentation.event.adapter.EventAdapter
import ru.woodymsk.socialapp.presentation.event.model.EventsEvent.ShowEvents
import javax.inject.Inject

class EventScreenFragment : Fragment(), BackButtonListener {

    companion object {
        fun newInstance() = EventScreenFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: EventViewModel
    private lateinit var binding: FragmentEventScreenBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[EventViewModel::class.java]
    }

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