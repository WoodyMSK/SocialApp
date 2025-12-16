package ru.woodymsk.socialapp.di

import dagger.Binds
import dagger.Module
import ru.woodymsk.socialapp.domain.common.GrammaticalFormatter
import ru.woodymsk.socialapp.presentation.common.GrammaticalFormatterImpl
import javax.inject.Singleton

@Module
interface FormatterModule {

    @Binds
    @Singleton
    fun bindGrammaticalFormatter(impl: GrammaticalFormatterImpl): GrammaticalFormatter
}