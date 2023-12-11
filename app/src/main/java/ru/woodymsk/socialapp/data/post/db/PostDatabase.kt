package ru.woodymsk.socialapp.data.post.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.woodymsk.socialapp.data.post.model.PostEntity
import ru.woodymsk.socialapp.data.post.model.PostKeyEntity

@Database(entities = [PostEntity::class, PostKeyEntity::class], version = 2, exportSchema = false)
@TypeConverters(PostConverters::class)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postKeyDao(): PostKeyDao
}