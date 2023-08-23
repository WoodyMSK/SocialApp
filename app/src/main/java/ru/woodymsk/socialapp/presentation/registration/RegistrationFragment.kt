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
import ru.woodymsk.socialapp.presentation.my_profile.MyProfileScreenFragment
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationFormState.LoginError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationFormState.PasswordError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationFormState.NameError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationFormState.IsRegistrationDataValid
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationFormState.ConfirmPasswordError
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationResult.Success
import ru.woodymsk.socialapp.presentation.registration.model.RegistrationResult.Error

@AndroidEntryPoint
class RegistrationScreenFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = RegistrationScreenFragment()
    }

    private val viewModel: RegistrationViewModel by viewModels()
    private val textChangedListener = TextChangedListener { registrationDataChecked() }
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

            observeRegistrationFormState()
            observeRegistrationResult()
            setListeners()

            binding.bRegistrationScreenSignInButton.setOnClickListener { registration() }
    }

    private fun showGreetingToast(userName: String) {
        val welcome = getString(welcome)
        Toast.makeText(requireActivity(), "$welcome, $userName.", Toast.LENGTH_LONG).show()
    }

    private fun observeRegistrationFormState() {
        viewModel.registrationFormState.observe(viewLifecycleOwner) { registrationFormState ->

            with(binding) {
                bRegistrationScreenSignInButton.isEnabled = false

                when (registrationFormState) {
                    is LoginError -> {
                        etRegistrationScreenAddLogin.error =
                            getString(registrationFormState.loginError)
                    }

                    is PasswordError -> {
                        etRegistrationScreenAddPassword.error =
                            getString(registrationFormState.passwordError)
                    }

                    is ConfirmPasswordError -> {
                        etRegistrationScreenConfirmPassword.error =
                            getString(registrationFormState.confirmPasswordError)
                    }

                    is NameError -> {
                        etRegistrationScreenAddName.error =
                            getString(registrationFormState.nameError)
                    }

                    is IsRegistrationDataValid -> bRegistrationScreenSignInButton.isEnabled = true
                }
            }
        }
    }

    private fun observeRegistrationResult() {
        viewModel.registrationResult.observe(viewLifecycleOwner) { registrationResult ->

            when (registrationResult) {
                is Success -> {
                    showGreetingToast(registrationResult.userName)
                    requireView().hideKeyboard()
                    navigator().navigateTo(MyProfileScreenFragment.newInstance())
                }

                is Error -> {
                    Toast.makeText(requireActivity(), registrationResult.error, Toast.LENGTH_LONG).show()
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

    private fun registrationDataChecked() {
        with(binding) {
            viewModel.registrationDataChecked(
                login = etRegistrationScreenAddLogin.text.toString(),
                password = etRegistrationScreenAddPassword.text.toString(),
                confirmPassword = etRegistrationScreenConfirmPassword.text.toString(),
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