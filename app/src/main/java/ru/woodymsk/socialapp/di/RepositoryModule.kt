package ru.woodymsk.socialapp.di

import dagger.Binds
import dagger.Module
import ru.woodymsk.socialapp.data.event.EventRepositoryImpl
import ru.woodymsk.socialapp.data.login.LoginRepositoryImpl
import ru.woodymsk.socialapp.data.post.PostRepositoryImpl
import ru.woodymsk.socialapp.data.profile.ProfileRepositoryImpl
import ru.woodymsk.socialapp.data.registration.RegistrationRepositoryImpl
import ru.woodymsk.socialapp.domain.event.EventRepository
import ru.woodymsk.socialapp.domain.login.LoginRepository
import ru.woodymsk.socialapp.domain.post.PostRepository
import ru.woodymsk.socialapp.domain.profile.ProfileRepository
import ru.woodymsk.socialapp.domain.registration.RegistrationRepository
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindEventRepository(impl: EventRepositoryImpl): EventRepository

    @Singleton
    @Binds
    fun bindLoginRepository(impl: LoginRepositoryImpl): LoginRepository

    @Singleton
    @Binds
    fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Singleton
    @Binds
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Singleton
    @Binds
    fun bindRegistrationRepository(impl: RegistrationRepositoryImpl): RegistrationRepository
}