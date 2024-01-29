package ru.woodymsk.socialapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.R.id.fragmentContainer
import ru.woodymsk.socialapp.R.id.itBottomNavigationAuthScreen
import ru.woodymsk.socialapp.R.id.itBottomNavigationEventScreen
import ru.woodymsk.socialapp.R.id.itBottomNavigationMyProfileScreen
import ru.woodymsk.socialapp.R.id.itBottomNavigationPostScreen
import ru.woodymsk.socialapp.data.auth.AppAuth
import ru.woodymsk.socialapp.databinding.ActivityMainBinding
import ru.woodymsk.socialapp.domain.navigation.BottomNavigation
import ru.woodymsk.socialapp.domain.navigation.subnavigation.LocalCiceroneHolder
import ru.woodymsk.socialapp.presentation.common.BackButtonListener
import ru.woodymsk.socialapp.presentation.common.Screens
import ru.woodymsk.socialapp.presentation.common.TabTag
import ru.woodymsk.socialapp.presentation.common.TabTag.AUTHORIZATION
import ru.woodymsk.socialapp.presentation.common.TabTag.EVENT_SCREEN
import ru.woodymsk.socialapp.presentation.common.TabTag.MY_PROFILE
import ru.woodymsk.socialapp.presentation.common.TabTag.POST_SCREEN
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigation, BackButtonListener {

    private val navigator: Navigator = object : AppNavigator(this, fragmentContainer) {
        override fun applyCommands(commands: Array<out Command>) {
            super.applyCommands(commands)
            supportFragmentManager.executePendingTransactions()
        }
    }

    @Inject
    lateinit var ciceroneHolder: LocalCiceroneHolder

    @Inject
    lateinit var auth: AppAuth

    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
            .also { setContentView(it.root) }

        initViews()

        if (savedInstanceState == null) {
            openTab(POST_SCREEN)
        }
    }

    override fun openTab(tabTag: TabTag) {
        val fragmentManager = supportFragmentManager
        var currentFragment: Fragment? = null
        val fragments = fragmentManager.fragments
        for (fragment in fragments) {
            if (fragment.isVisible) {
                currentFragment = fragment
                break
            }
        }
        val newFragment = fragmentManager.findFragmentByTag(tabTag.name)
        if (currentFragment != null && newFragment != null && currentFragment === newFragment) return
        val transaction = fragmentManager.beginTransaction()
        if (newFragment == null) {
            transaction.add(
                fragmentContainer,
                Screens.onTabContainerFragment(tabTag).createFragment(fragmentManager.fragmentFactory),
                tabTag.name,
            )
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        if (newFragment != null) {
            transaction.show(newFragment)
        }
        transaction.commitNow()
    }

    override fun openTabWithNavigationReset(tabTag: TabTag) {
        ciceroneHolder.clear()
        val transaction = supportFragmentManager.beginTransaction()
        for (fragment in supportFragmentManager.fragments) {
            if (fragment != null) {
                transaction.remove(fragment)
            }
        }
        transaction.commitNow()
        selectBottomNavigationButton(tabTag)
    }

    private fun initViews() {
        with(binding) {

            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    itBottomNavigationPostScreen -> openTab(POST_SCREEN)
                    itBottomNavigationEventScreen -> openTab(EVENT_SCREEN)
                    itBottomNavigationAuthScreen -> openTab(AUTHORIZATION)
                    itBottomNavigationMyProfileScreen -> openTab(MY_PROFILE)
                }
                true
            }

            lifecycleScope.launch {
                auth.authStateFlow.collectLatest {
                    bottomNavigationView.menu.findItem(itBottomNavigationMyProfileScreen)
                        .isVisible = it.id != 0

                    bottomNavigationView.menu.findItem(itBottomNavigationAuthScreen)
                        .isVisible = it.id == 0
                }
            }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        var fragment: Fragment? = null
        val fragments = fragmentManager.fragments
        for (activeFragment in fragments) {
            if (activeFragment.isVisible) {
                fragment = activeFragment
                break
            }
        }
        if (fragment != null && fragment is BackButtonListener) {
            (fragment as BackButtonListener).onBackPressed()
        }
    }

    private fun selectBottomNavigationButton(tabTag: TabTag) {
        with(binding) {
            when (tabTag) {
                POST_SCREEN -> {
                    bottomNavigationView.selectedItemId = itBottomNavigationPostScreen
                }
                EVENT_SCREEN -> {
                    bottomNavigationView.selectedItemId = itBottomNavigationEventScreen
                }
                AUTHORIZATION -> {
                    bottomNavigationView.selectedItemId = itBottomNavigationAuthScreen
                }
                MY_PROFILE -> {
                    bottomNavigationView.selectedItemId = itBottomNavigationMyProfileScreen
                }
            }
        }
    }
}