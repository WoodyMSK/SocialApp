package ru.woodymsk.socialapp.presentation.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.woodymsk.socialapp.databinding.FragmentNewPostBinding
import ru.woodymsk.socialapp.domain.focus

class NewPostFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = NewPostFragment()
    }

    private lateinit var binding: FragmentNewPostBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.etNewPostMessage.focus()
    }
}