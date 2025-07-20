package ru.woodymsk.socialapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.woodymsk.socialapp.data.event.db.EventDao
import ru.woodymsk.socialapp.data.event.db.EventDatabase
import javax.inject.Singleton

@Module
object EventDatabaseModule {

    @Singleton
    @Provides
    fun provideEventDatabase(
        context: Context
    ): EventDatabase =
        Room.databaseBuilder(context, EventDatabase::class.java, "event.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideEventDao(eventDb: EventDatabase): EventDao = eventDb.EventDao()
}