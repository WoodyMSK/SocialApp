package ru.woodymsk.socialapp.domain.application

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import ru.woodymsk.socialapp.di.DaggerAppComponent
import javax.inject.Inject

class SocialApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        DaggerAppComponent
            .builder()
            .application(this)
            .context(this)
            .build()
            .inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    companion object {
        private lateinit var INSTANCE: SocialApp
    }
}