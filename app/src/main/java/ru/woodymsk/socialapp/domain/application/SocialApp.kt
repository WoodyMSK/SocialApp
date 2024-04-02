package ru.woodymsk.socialapp.domain.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class SocialApp : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        private lateinit var INSTANCE: SocialApp
    }
}