package ru.woodymsk.socialapp.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.woodymsk.socialapp.presentation.auth.AuthFragment
import ru.woodymsk.socialapp.presentation.event.EventScreenFragment
import ru.woodymsk.socialapp.presentation.login.LoginFragment
import ru.woodymsk.socialapp.presentation.new_post.NewPostFragment
import ru.woodymsk.socialapp.presentation.post.PostScreenFragment
import ru.woodymsk.socialapp.presentation.profile.ProfileScreenFragment
import ru.woodymsk.socialapp.presentation.registration.RegistrationFragment

@Module
interface FragmentModule {
    @ContributesAndroidInjector
    fun contributeAuthFragment(): AuthFragment

    @ContributesAndroidInjector
    fun contributeEventScreenFragment(): EventScreenFragment

    @ContributesAndroidInjector
    fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    fun contributeNewPostFragment(): NewPostFragment

    @ContributesAndroidInjector
    fun contributePostScreenFragment(): PostScreenFragment

    @ContributesAndroidInjector
    fun contributeProfileScreenFragment(): ProfileScreenFragment

    @ContributesAndroidInjector
    fun contributeRegistrationFragment(): RegistrationFragment
}