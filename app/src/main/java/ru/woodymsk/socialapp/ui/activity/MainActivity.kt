package ru.woodymsk.socialapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.databinding.ActivityMainBinding
import ru.woodymsk.socialapp.ui.fragment.StartScreenFragment

class MainActivity : AppCompatActivity() {

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
}