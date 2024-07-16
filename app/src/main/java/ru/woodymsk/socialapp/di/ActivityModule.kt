package ru.woodymsk.socialapp.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.woodymsk.socialapp.presentation.activity.MainActivity

@Module
interface ActivityModule {
    @ContributesAndroidInjector
    fun contributeMainActivity(): MainActivity
}