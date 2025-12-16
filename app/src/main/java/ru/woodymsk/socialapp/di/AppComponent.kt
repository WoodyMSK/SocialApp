package ru.woodymsk.socialapp.di

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import ru.woodymsk.socialapp.domain.application.SocialApp
import ru.woodymsk.socialapp.presentation.activity.MainActivity
import ru.woodymsk.socialapp.presentation.auth.AuthFragment
import ru.woodymsk.socialapp.presentation.event.EventScreenFragment
import ru.woodymsk.socialapp.presentation.login.LoginFragment
import ru.woodymsk.socialapp.presentation.new_post.NewPostFragment
import ru.woodymsk.socialapp.presentation.post.PostScreenFragment
import ru.woodymsk.socialapp.presentation.profile.ProfileScreenFragment
import ru.woodymsk.socialapp.presentation.registration.RegistrationFragment
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ApiModule::class,
        AppModule::class,
        RepositoryModule::class,
        NavigationModule::class,
        PostDatabaseModule::class,
        EventDatabaseModule::class,
        ViewModelModule::class,
        ActivityModule::class,
        FragmentModule::class,
        ResourceModule::class,
        ConverterModule::class,
        FormatterModule::class,
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }

    fun inject(socialApp: SocialApp)
    fun inject(mainActivity: MainActivity)
    fun inject(authFragment: AuthFragment)
    fun inject(eventScreenFragment: EventScreenFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(newPostFragment: NewPostFragment)
    fun inject(postFragment: PostScreenFragment)
    fun inject(profileFragment: ProfileScreenFragment)
    fun inject(registrationFragment: RegistrationFragment)
}