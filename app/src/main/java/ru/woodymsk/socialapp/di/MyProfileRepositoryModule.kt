package ru.woodymsk.socialapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.woodymsk.socialapp.data.my_profile.MyProfileRepositoryImpl
import ru.woodymsk.socialapp.domain.my_profile.MyProfileRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface MyProfileRepositoryModule {

    @Singleton
    @Binds
    fun bindMyProfileRepository(impl: MyProfileRepositoryImpl): MyProfileRepository
}