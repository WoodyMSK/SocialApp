package ru.woodymsk.socialapp.presentation.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.woodymsk.socialapp.presentation.auth.AuthFragment
import ru.woodymsk.socialapp.presentation.event.EventScreenFragment
import ru.woodymsk.socialapp.presentation.login.LoginFragment
import ru.woodymsk.socialapp.presentation.my_profile.MyProfileScreenFragment
import ru.woodymsk.socialapp.presentation.post.NewPostFragment
import ru.woodymsk.socialapp.presentation.post.PostScreenFragment
import ru.woodymsk.socialapp.presentation.registration.RegistrationFragment

object Screens {

    fun postScreen() = FragmentScreen { PostScreenFragment.newInstance() }
    fun eventScreen() = FragmentScreen { EventScreenFragment.newInstance() }
    fun authScreen() = FragmentScreen { AuthFragment.newInstance() }
    fun profileScreen() = FragmentScreen { MyProfileScreenFragment.newInstance() }
    fun newPostScreen() = FragmentScreen { NewPostFragment.newInstance() }
    fun loginScreen() = FragmentScreen { LoginFragment.newInstance() }
    fun registrationScreen() = FragmentScreen { RegistrationFragment.newInstance() }

}