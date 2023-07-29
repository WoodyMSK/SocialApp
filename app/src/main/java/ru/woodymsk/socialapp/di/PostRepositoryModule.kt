package ru.woodymsk.socialapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.woodymsk.socialapp.data.post_screen.PostRepositoryImpl
import ru.woodymsk.socialapp.domain.post_screen.PostRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface PostRepositoryModule {

    @Singleton
    @Binds
    fun bindPostRepository(impl: PostRepositoryImpl): PostRepository
}