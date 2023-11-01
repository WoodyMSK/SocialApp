package ru.woodymsk.socialapp.data.post.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.woodymsk.socialapp.data.post.model.PostKeyEntity

@Dao
interface PostKeyDao {
    @Query("SELECT MAX(id) FROM PostKeyEntity")
    suspend fun maxKey(): Int?

    @Query("SELECT MIN(id) FROM PostKeyEntity")
    suspend fun minKey(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostKey(postKeyEntity: PostKeyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostKeys(postKeysEntity: List<PostKeyEntity>)

    @Query("DELETE FROM PostKeyEntity")
    suspend fun removeAllPostKeys()
}