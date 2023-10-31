package ru.woodymsk.socialapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.woodymsk.socialapp.data.post.db.PostDao
import ru.woodymsk.socialapp.data.post.db.PostDatabase
import ru.woodymsk.socialapp.data.post.db.PostKeyDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PostDatabaseModule {

    @Singleton
    @Provides
    fun providePostDatabase(
        @ApplicationContext
        context: Context
    ): PostDatabase = Room.databaseBuilder(context, PostDatabase::class.java, "post.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun providePostDao(postDb: PostDatabase): PostDao = postDb.postDao()

    @Provides
    fun providePostKeyDao(postKeyDb: PostDatabase): PostKeyDao = postKeyDb.postKeyDao()
}