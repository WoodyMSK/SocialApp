package ru.woodymsk.socialapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.databinding.ActivityMainBinding
import ru.woodymsk.socialapp.ui.start_screen.StartScreenFragment
import ru.woodymsk.socialapp.ui.navigation.Navigator

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
            .also { setContentView(it.root) }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, StartScreenFragment())
                .commit()
        }
    }

    override fun navigateTo(fragment: Fragment) {
        launchFragment(fragment)
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

}