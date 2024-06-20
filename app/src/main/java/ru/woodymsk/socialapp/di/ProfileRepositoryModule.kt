package ru.woodymsk.socialapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.woodymsk.socialapp.data.profile.ProfileRepositoryImpl
import ru.woodymsk.socialapp.domain.profile.ProfileRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface ProfileRepositoryModule {

    @Singleton
    @Binds
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}