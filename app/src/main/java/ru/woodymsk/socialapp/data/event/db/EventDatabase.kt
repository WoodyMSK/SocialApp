package ru.woodymsk.socialapp.data.event.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.woodymsk.socialapp.data.event.model.EventEntity
import ru.woodymsk.socialapp.data.post.db.PostConverters

@Database(entities = [EventEntity::class], version = 1  , exportSchema = false)
@TypeConverters(PostConverters::class)
abstract class EventDatabase : RoomDatabase() {
    abstract fun EventDao(): EventDao
}