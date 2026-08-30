package ru.woodymsk.socialapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.woodymsk.socialapp.presentation.common.compose.AudioPlayerManager
import javax.inject.Singleton

@Module
class AudioPlayerModule {
    @Provides
    @Singleton
    fun provideAudioPlayerManager(
        context: Context
    ): AudioPlayerManager {
        return AudioPlayerManager(context)
    }
}