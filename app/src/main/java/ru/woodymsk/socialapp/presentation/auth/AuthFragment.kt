package ru.woodymsk.socialapp.presentation.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import ru.woodymsk.socialapp.databinding.FragmentAuthBinding
import ru.woodymsk.socialapp.presentation.common.BackButtonListener
import ru.woodymsk.socialapp.presentation.common.ViewModelFactory
import javax.inject.Inject

class AuthFragment : Fragment(), BackButtonListener {

    companion object {
        fun newInstance() = AuthFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentAuthBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAuthBinding.inflate(inflater, container, false)

        with(binding) {
            bAuthScreenLogIn.setOnClickListener { onLoginClick() }
            bAuthRegistration.setOnClickListener { onRegistrationClick() }
        }

        return binding.root
    }

    override fun onBackPressed() = viewModel.onBackPressed()

    private fun onLoginClick() = viewModel.onLoginClick()

    private fun onRegistrationClick() = viewModel.onRegistrationClick()

}