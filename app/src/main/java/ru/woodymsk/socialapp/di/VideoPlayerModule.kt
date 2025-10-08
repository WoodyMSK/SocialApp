package ru.woodymsk.socialapp.di

import dagger.Module
import dagger.Provides
import ru.woodymsk.socialapp.presentation.common.compose.VideoPlayerManager
import javax.inject.Singleton

@Module
object VideoPlayerModule {
    @Provides
    @Singleton
    fun provideVideoPlayerManager(
        videoPlayerManager: VideoPlayerManager
    ): VideoPlayerManager {
        return videoPlayerManager
    }
}