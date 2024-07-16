package ru.woodymsk.socialapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.woodymsk.socialapp.data.post.db.PostDao
import ru.woodymsk.socialapp.data.post.db.PostDatabase
import ru.woodymsk.socialapp.data.post.db.PostKeyDao
import javax.inject.Singleton

@Module
object PostDatabaseModule {

    @Singleton
    @Provides
    fun providePostDatabase(
        context: Context
    ): PostDatabase = Room.databaseBuilder(context, PostDatabase::class.java, "post.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun providePostDao(postDb: PostDatabase): PostDao = postDb.postDao()

    @Provides
    fun providePostKeyDao(postKeyDb: PostDatabase): PostKeyDao = postKeyDb.postKeyDao()
}