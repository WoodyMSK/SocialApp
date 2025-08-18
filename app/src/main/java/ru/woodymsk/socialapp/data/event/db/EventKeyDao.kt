package ru.woodymsk.socialapp.data.event.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.woodymsk.socialapp.data.event.model.EventKeyEntity

@Dao
interface EventKeyDao {
    @Query("SELECT MAX(id) FROM EventKeyEntity")
    suspend fun maxKey(): Int?

    @Query("SELECT MIN(id) FROM EventKeyEntity")
    suspend fun minKey(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventKey(eventKeyEntity: EventKeyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventKeys(eventKeysEntity: List<EventKeyEntity>)

    @Query("DELETE FROM EventKeyEntity")
    suspend fun removeAllEventKeys()
}