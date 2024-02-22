package ru.woodymsk.socialapp.presentation.common

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.woodymsk.socialapp.presentation.auth.AuthFragment
import ru.woodymsk.socialapp.presentation.event.EventScreenFragment
import ru.woodymsk.socialapp.presentation.login.LoginFragment
import ru.woodymsk.socialapp.presentation.my_profile.MyProfileScreenFragment
import ru.woodymsk.socialapp.presentation.new_post.NewPostFragment
import ru.woodymsk.socialapp.presentation.post.PostScreenFragment
import ru.woodymsk.socialapp.presentation.registration.RegistrationFragment

object Screens {
    fun onTabContainerFragment(tabTag: TabTag) = FragmentScreen {
        TabContainerFragment.newInstance(tabTag)
    }

    fun onPostScreenTab() = FragmentScreen {
        PostScreenFragment.newInstance()
    }

    fun onEventScreenTab() = FragmentScreen {
        EventScreenFragment.newInstance()
    }

    fun onAuthScreenTab() = FragmentScreen {
        AuthFragment.newInstance()
    }

    fun onMyProfileScreenTab() = FragmentScreen {
        MyProfileScreenFragment.newInstance()
    }

    fun onNewPostScreenTab() = FragmentScreen {
        NewPostFragment.newInstance()
    }

    fun onLoginScreenTab() = FragmentScreen {
        LoginFragment.newInstance()
    }

    fun onRegistrationScreenTab() = FragmentScreen {
        RegistrationFragment.newInstance()
    }
}