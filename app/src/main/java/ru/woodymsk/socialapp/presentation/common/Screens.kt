package ru.woodymsk.socialapp.presentation.common

import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.woodymsk.socialapp.presentation.auth.AuthFragment
import ru.woodymsk.socialapp.presentation.event.EventScreenFragment
import ru.woodymsk.socialapp.presentation.login.LoginFragment
import ru.woodymsk.socialapp.presentation.my_profile.MyProfileScreenFragment
import ru.woodymsk.socialapp.presentation.new_post.NewPostFragment
import ru.woodymsk.socialapp.presentation.post.PostScreenFragment
import ru.woodymsk.socialapp.presentation.registration.RegistrationFragment

object Screens {

    val screens = HashMap<Int, MutableList<Screen>>()

    init {
        createScreenList()
    }

    fun postScreen() = FragmentScreen { PostScreenFragment.newInstance() }
    fun eventScreen() = FragmentScreen { EventScreenFragment.newInstance() }
    fun authScreen() = FragmentScreen { AuthFragment.newInstance() }
    fun profileScreen() = FragmentScreen { MyProfileScreenFragment.newInstance() }
    fun newPostScreen() = FragmentScreen { NewPostFragment.newInstance() }
    fun loginScreen() = FragmentScreen { LoginFragment.newInstance() }
    fun registrationScreen() = FragmentScreen { RegistrationFragment.newInstance() }

    /**
     * Небольшой костыль: каждый новый экран нужно добавлять под свой пункт в навбаре:
     * 1 - Лента
     * 2 - События
     * 3 - Вход
     * 4 - Профиль
     *
     * Это необходимо для того, чтобы при навигации не через навбар фокус иконки переходил на
     * нужную вкладку навбара
     */
    private fun createScreenList() {
        Screens.let {
            screens.computeIfAbsent(0) { mutableListOf() }.apply {
                add(it.postScreen())
                add(it.newPostScreen())
            }
            screens.computeIfAbsent(1) { mutableListOf() }.apply {
                add(it.eventScreen())
            }
            screens.computeIfAbsent(2) { mutableListOf() }.apply {
                add(it.authScreen())
                add(it.loginScreen())
                add(it.registrationScreen())
            }
            screens.computeIfAbsent(3) { mutableListOf() }.apply {
                add(it.profileScreen())
            }
        }
    }
}