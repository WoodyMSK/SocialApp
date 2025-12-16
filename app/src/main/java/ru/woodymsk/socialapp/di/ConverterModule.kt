package ru.woodymsk.socialapp.di

import dagger.Binds
import dagger.Module
import ru.woodymsk.socialapp.domain.map_screen.converter.ObjectDetailsConverter
import ru.woodymsk.socialapp.presentation.map_screen.converter.ObjectDetailsConverterImpl
import javax.inject.Singleton

@Module
interface ConverterModule {

    @Binds
    @Singleton
    fun bindObjectDetailsConverter(impl: ObjectDetailsConverterImpl): ObjectDetailsConverter
}