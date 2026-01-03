package ru.woodymsk.socialapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.woodymsk.socialapp.presentation.common.ResourcesService
import javax.inject.Singleton

@Module
object ResourceModule {

    @Provides
    @Singleton
    fun provideResourcesService(context: Context): ResourcesService = ResourcesService(context)
}