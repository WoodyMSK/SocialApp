package ru.woodymsk.socialapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.R.id.fragmentContainer
import ru.woodymsk.socialapp.R.id.itBottomNavigationEventScreen
import ru.woodymsk.socialapp.R.id.itBottomNavigationLogInScreen
import ru.woodymsk.socialapp.R.id.itBottomNavigationMyProfileScreen
import ru.woodymsk.socialapp.R.id.itBottomNavigationPostScreen
import ru.woodymsk.socialapp.databinding.ActivityMainBinding
import ru.woodymsk.socialapp.domain.Navigator
import ru.woodymsk.socialapp.presentation.auth.AuthFragment
import ru.woodymsk.socialapp.presentation.event.EventScreenFragment
import ru.woodymsk.socialapp.presentation.my_profile.MyProfileScreenFragment
import ru.woodymsk.socialapp.presentation.post.PostScreenFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator {

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

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                itBottomNavigationPostScreen -> {
                    navigateTo(PostScreenFragment.newInstance(), false)
                }
                itBottomNavigationEventScreen -> {
                    navigateTo(EventScreenFragment.newInstance(), false)
                }
                itBottomNavigationLogInScreen -> {
                    navigateTo(AuthFragment.newInstance(), false)
                }
                itBottomNavigationMyProfileScreen -> {
                    navigateTo(MyProfileScreenFragment.newInstance(), false)
                }
            }
            true
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