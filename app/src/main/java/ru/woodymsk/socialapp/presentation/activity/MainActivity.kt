package ru.woodymsk.socialapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.woodymsk.socialapp.R.id.*
import ru.woodymsk.socialapp.databinding.ActivityMainBinding
import ru.woodymsk.socialapp.domain.Navigator
import ru.woodymsk.socialapp.presentation.event_screen.EventScreenFragment
import ru.woodymsk.socialapp.presentation.my_profile_screen.MyProfileScreenFragment
import ru.woodymsk.socialapp.presentation.post_screen.PostScreenFragment
import ru.woodymsk.socialapp.presentation.start_screen.StartScreenFragment

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
                    navigateTo(StartScreenFragment.newInstance(), false)
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