package ru.woodymsk.socialapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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
import ru.woodymsk.socialapp.domain.Navigator
import ru.woodymsk.socialapp.presentation.auth.AuthFragment
import ru.woodymsk.socialapp.presentation.event.EventScreenFragment
import ru.woodymsk.socialapp.presentation.my_profile.MyProfileScreenFragment
import ru.woodymsk.socialapp.presentation.post.PostScreenFragment
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator {

    @Inject
    lateinit var auth: AppAuth

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
            .also { setContentView(it.root) }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(fragmentContainer, PostScreenFragment())
                .commit()
        }

        with(binding) {

            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    itBottomNavigationPostScreen -> {
                        navigateTo(
                            fragment = PostScreenFragment.newInstance(),
                            addToBackStack = false
                        )
                    }
                    itBottomNavigationEventScreen -> {
                        navigateTo(
                            fragment = EventScreenFragment.newInstance(),
                            addToBackStack = false
                        )
                    }
                    itBottomNavigationAuthScreen -> {
                        navigateTo(
                            fragment = AuthFragment.newInstance(),
                            addToBackStack = false)
                    }
                    itBottomNavigationMyProfileScreen -> {
                        navigateTo(
                            fragment = MyProfileScreenFragment.newInstance(),
                            addToBackStack = false
                        )
                    }
                }
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

    override fun navigateTo(fragment: Fragment, addToBackStack: Boolean) {
        launchFragment(fragment, addToBackStack)
    }

    private fun launchFragment(fragment: Fragment, addToBackStack: Boolean) {
        supportFragmentManager
            .beginTransaction()
            .also {
                if (addToBackStack) it.addToBackStack(null)
            }
            .replace(fragmentContainer, fragment)
            .commit()
    }

}