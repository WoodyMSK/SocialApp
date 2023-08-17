package ru.woodymsk.socialapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.woodymsk.socialapp.data.registration.RegistrationRepositoryImpl
import ru.woodymsk.socialapp.domain.registration.RegistrationRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RegistrationRepositoryModule {

    @Singleton
    @Binds
    fun bindRegistrationRepository(impl: RegistrationRepositoryImpl): RegistrationRepository
}