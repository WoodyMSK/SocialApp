package ru.woodymsk.socialapp.data.event.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.woodymsk.socialapp.data.event.model.EventEntity
import ru.woodymsk.socialapp.data.event.model.EventKeyEntity
import ru.woodymsk.socialapp.data.post.db.PostConverters

@Database(entities = [EventEntity::class, EventKeyEntity::class], version = 3  , exportSchema = false)
@TypeConverters(PostConverters::class)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun eventKeyDao(): EventKeyDao
}