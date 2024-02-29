package ru.woodymsk.socialapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.BackTo
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.Forward
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.R.id.itBottomNavigationAuthScreen
import ru.woodymsk.socialapp.R.id.itBottomNavigationEventScreen
import ru.woodymsk.socialapp.R.id.itBottomNavigationMyProfileScreen
import ru.woodymsk.socialapp.R.id.itBottomNavigationPostScreen
import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.databinding.ActivityMainBinding
import ru.woodymsk.socialapp.presentation.navigation.Screens
import ru.woodymsk.socialapp.presentation.navigation.Screens.authScreen
import ru.woodymsk.socialapp.presentation.navigation.Screens.eventScreen
import ru.woodymsk.socialapp.presentation.navigation.Screens.postScreen
import ru.woodymsk.socialapp.presentation.navigation.Screens.profileScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var auth: AppAuth

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private lateinit var binding: ActivityMainBinding

    private val screens = HashMap<Int, MutableList<Screen>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
            .also { setContentView(it.root) }
        createScreenList()

        with(binding) {

            val navigator: Navigator =
                object : AppNavigator(this@MainActivity, fragmentContainer.id) {
                    override fun applyCommands(commands: Array<out Command>) {
                        super.applyCommands(commands)
                        for (command in commands) {
                            when (command) {
                                is Forward -> selectBottomMenuItems(command.screen)
                                is Replace -> selectBottomMenuItems(command.screen)
                                is BackTo -> command.screen?.let { selectBottomMenuItems(it) }
                            }
                        }
                        supportFragmentManager.executePendingTransactions()
                    }
                }

            navigatorHolder.setNavigator(navigator)
            router.replaceScreen(postScreen())

            bottomNavigationView.setOnItemSelectedListener {
                router.replaceScreen(
                    when (it.itemId) {
                        itBottomNavigationPostScreen -> postScreen()
                        itBottomNavigationEventScreen -> eventScreen()
                        itBottomNavigationAuthScreen -> authScreen()
                        itBottomNavigationMyProfileScreen -> profileScreen()
                        else -> postScreen()
                    }
                )
                true
            }

            lifecycleScope.launch {
                auth.authStateFlow.collectLatest { it ->
                    bottomNavigationView.menu.findItem(itBottomNavigationMyProfileScreen)
                        .isVisible = it.id != 0

                    bottomNavigationView.menu.findItem(itBottomNavigationAuthScreen)
                        .isVisible = it.id == 0
                }
            }
        }
    }

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

    private fun selectBottomMenuItems(screen: Screen) {
        for ((key, elementList) in screens) {
            for (element in elementList) {
                if (element.screenKey == screen.screenKey) {
                    binding.bottomNavigationView.menu.getItem(key).isChecked = true
                }
            }
        }
    }
}