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

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removePostById(id: Int)

    @Query ("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean

    @Query("""
        UPDATE PostEntity SET
        likes = likes + 1,
        likedByMe = 1
        WHERE id = :id
    """)
    fun like(id: Int)

    @Query("""
        UPDATE PostEntity SET
        likes = likes - 1,
        likedByMe = 0
        WHERE id = :id
    """)
    fun deleteLike(id: Int)
}