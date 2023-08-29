package ru.woodymsk.socialapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.woodymsk.socialapp.data.login.LoginRepositoryImpl
import ru.woodymsk.socialapp.domain.login.LoginRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface LoginRepositoryModule {

    @Singleton
    @Binds
    fun bindLoginRepository(impl: LoginRepositoryImpl): LoginRepository
}