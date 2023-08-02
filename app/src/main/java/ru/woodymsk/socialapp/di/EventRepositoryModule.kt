package ru.woodymsk.socialapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.woodymsk.socialapp.data.event.EventRepositoryImpl
import ru.woodymsk.socialapp.domain.event.EventRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface EventRepositoryModule {

    @Singleton
    @Binds
    fun bindEventRepository(impl: EventRepositoryImpl): EventRepository
}