package ru.woodymsk.socialapp.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.databinding.FragmentLoginBinding
import ru.woodymsk.socialapp.domain.hideKeyboard
import ru.woodymsk.socialapp.presentation.common.TextChangedListener
import ru.woodymsk.socialapp.presentation.login.model.LoginEvents.LoginDataError
import ru.woodymsk.socialapp.presentation.login.model.LoginEvents.LoginDataValid
import ru.woodymsk.socialapp.presentation.login.model.LoginEvents.LoginError
import ru.woodymsk.socialapp.presentation.login.model.LoginEvents.LoginSuccess
import ru.woodymsk.socialapp.presentation.login.model.LoginEvents.PasswordDataError

@AndroidEntryPoint
class LoginFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()
    private val textChangedListener = TextChangedListener {
        with(binding) {
            viewModel.loginDataChecked(
                login = etLoginScreenAddLogin.text.toString(),
                password = etLoginScreenAddPassword.text.toString(),
            )
        }
    }
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoginEvents()
        setListeners()

        binding.bLoginEnter.setOnClickListener { login() }
    }

    private fun observeLoginEvents() {
        viewModel.loginEvents.observe(viewLifecycleOwner) { event ->

            with(binding) {
                bLoginEnter.isEnabled = false

                when (event) {
                    is LoginDataValid -> bLoginEnter.isEnabled = true

                    is LoginDataError -> {
                        etLoginScreenAddLogin.error = getString(event.loginError)
                    }

                    is PasswordDataError -> {
                        etLoginScreenAddPassword.error = getString(event.passwordError)
                    }

                    is LoginSuccess -> {
                        Toast.makeText(requireActivity(), event.greeting, Toast.LENGTH_LONG).show()
                        requireView().hideKeyboard()
                        viewModel.goToProfileScreen()
                    }

                    is LoginError -> {
                        Toast.makeText(requireActivity(), event.appError.code, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun login() {
        with(binding) {
            viewModel.login(
                login = etLoginScreenAddLogin.text.toString(),
                password = etLoginScreenAddPassword.text.toString(),
            )
        }
    }

    private fun setListeners() {
        with(binding) {
            etLoginScreenAddLogin.addTextChangedListener(textChangedListener)
            etLoginScreenAddPassword.addTextChangedListener(textChangedListener)
            etLoginScreenAddPassword.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) login()
                false
            }
        }
    }
}