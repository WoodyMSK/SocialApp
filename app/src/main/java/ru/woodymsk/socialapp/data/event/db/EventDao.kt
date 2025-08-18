package ru.woodymsk.socialapp.data.event.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.woodymsk.socialapp.data.event.model.EventEntity

@Dao
interface EventDao {

    @Query("SELECT * FROM EventEntity ORDER BY id DESC")
    fun getPagingSource(): PagingSource<Int, EventEntity>

    @Query("SELECT * FROM EventEntity ORDER BY id DESC")
    fun getEventFlow() : Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventList(eventList: List<EventEntity>)

    @Query("DELETE FROM EventEntity WHERE id = :id")
    suspend fun removeEventById(id: Int)

    @Query("DELETE FROM EventEntity WHERE id IN (:ids)")
    suspend fun removeEventsByIds(ids: List<Int>)

    @Query("DELETE FROM EventEntity")
    suspend fun removeAllEvents()

    @Query ("SELECT COUNT(*) == 0 FROM EventEntity")
    suspend fun isEmpty(): Boolean
}