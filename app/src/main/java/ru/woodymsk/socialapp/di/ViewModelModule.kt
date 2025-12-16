package ru.woodymsk.socialapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import ru.woodymsk.socialapp.presentation.auth.AuthViewModel
import ru.woodymsk.socialapp.presentation.common.ViewModelFactory
import ru.woodymsk.socialapp.presentation.event.EventListViewModel
import ru.woodymsk.socialapp.presentation.event_details.EventDetailsViewModel
import ru.woodymsk.socialapp.presentation.login.LoginViewModel
import ru.woodymsk.socialapp.presentation.map_screen.MapScreenViewModel
import ru.woodymsk.socialapp.presentation.new_event.NewEventViewModel
import ru.woodymsk.socialapp.presentation.new_post.NewPostViewModel
import ru.woodymsk.socialapp.presentation.post.PostViewModel
import ru.woodymsk.socialapp.presentation.profile.ProfileViewModel
import ru.woodymsk.socialapp.presentation.registration.RegistrationViewModel
import kotlin.reflect.KClass


@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostViewModel::class)
    fun bindPostViewModel(postViewModel: PostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventListViewModel::class)
    fun bindEventListViewModel(eventListViewModel: EventListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewPostViewModel::class)
    fun bindNewPostViewModel(newPosViewModel: NewPostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    fun bindRegistrationViewModel(registrationViewModel: RegistrationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewEventViewModel::class)
    fun bindNewEventViewModel(newEventViewModel: NewEventViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventDetailsViewModel::class)
    fun bindEventDetailsViewModel(eventDetailsViewModel: EventDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapScreenViewModel::class)
    fun bindMapScreenViewModel(mapScreenViewModel: MapScreenViewModel): ViewModel
}

@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)