package ru.woodymsk.socialapp.data.post.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.woodymsk.socialapp.data.post.model.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getPagingSource(): PagingSource<Int, PostEntity>

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAllPostList(): List<PostEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(postEntity: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostList(postsEntity: List<PostEntity>)

    @Query("DELETE FROM PostEntity")
    suspend fun removeAllPosts()
}