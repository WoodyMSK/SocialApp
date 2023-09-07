package ru.woodymsk.socialapp.presentation.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.R.string.welcome
import ru.woodymsk.socialapp.databinding.FragmentRegistrationBinding
import ru.woodymsk.socialapp.domain.hideKeyboard
import ru.woodymsk.socialapp.domain.navigator
import ru.woodymsk.socialapp.presentation.common.TextChangedListener
import ru.woodymsk.socialapp.presentation.my_profile.MyProfileScreenFragment
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationEvents.LoginDataError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationEvents.PasswordDataError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationEvents.NameDataError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationEvents.RegistrationDataValid
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationEvents.ConfirmPasswordError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationEvents.RegistrationSuccess
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationEvents.RegistrationError

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = RegistrationFragment()
    }

    private val viewModel: RegistrationViewModel by viewModels()
    private val textChangedListener = TextChangedListener {
        with(binding) {
            viewModel.registrationDataChecked(
                login = etRegistrationScreenAddLogin.text.toString(),
                password = etRegistrationScreenAddPassword.text.toString(),
                confirmPassword = etRegistrationScreenConfirmPassword.text.toString(),
                userName = etRegistrationScreenAddName.text.toString(),
            )
        }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeRegistrationEvents()
        setListeners()

        binding.bRegistrationScreenSignInButton.setOnClickListener { registration() }
    }

    private fun showGreetingToast(userName: String) {
        val welcome = getString(welcome)
        Toast.makeText(requireActivity(), "$welcome, $userName.", Toast.LENGTH_LONG).show()
    }

    private fun observeRegistrationEvents() {
        viewModel.registrationEvents.observe(viewLifecycleOwner) { event ->


            with(binding) {
                bRegistrationScreenSignInButton.isEnabled = false
                etRegistrationScreenConfirmPassword.error = null

                when (event) {
                    is LoginDataError -> {
                        etRegistrationScreenAddLogin.error =
                            getString(event.loginError)
                    }

                    is PasswordDataError -> {
                        etRegistrationScreenAddPassword.error =
                            getString(event.passwordError)
                    }

                    is ConfirmPasswordError -> {
                        etRegistrationScreenConfirmPassword.error =
                            getString(event.confirmPasswordError)
                    }

                    is NameDataError -> {
                        etRegistrationScreenAddName.error =
                            getString(event.nameError)
                    }

                    is RegistrationSuccess -> {
                        showGreetingToast(event.userName)
                        requireView().hideKeyboard()
                        navigator().navigateTo(MyProfileScreenFragment.newInstance())
                    }

                    is RegistrationError -> {
                        Toast.makeText(
                            requireActivity(),
                            event.appError.code,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is RegistrationDataValid -> bRegistrationScreenSignInButton.isEnabled = true
                }
            }
        }
    }

    private fun registration() {
        with(binding) {
            viewModel.registration(
                login = etRegistrationScreenAddLogin.text.toString(),
                password = etRegistrationScreenAddPassword.text.toString(),
                userName = etRegistrationScreenAddName.text.toString(),
            )
        }
    }

    private fun setListeners() {
        with(binding) {
            etRegistrationScreenAddLogin.addTextChangedListener(textChangedListener)
            etRegistrationScreenConfirmPassword.addTextChangedListener(textChangedListener)
            etRegistrationScreenAddName.addTextChangedListener(textChangedListener)
            etRegistrationScreenAddPassword.addTextChangedListener(textChangedListener)
            etRegistrationScreenAddName.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registration()
                }
                false
            }
        }
    }
}