package ru.woodymsk.socialapp.domain.post.mapper

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.woodymsk.socialapp.domain.post.model.Post
import ru.woodymsk.socialapp.data.post.model.PostEntity
import javax.inject.Inject

class PostMapper @Inject constructor() {

    fun mapPostToDao(items: List<Post>): List<PostEntity> = items.map {
        PostEntity(
            id = it.id,
            authorId = it.authorId,
            author = it.author,
            authorAvatar = it.authorAvatar,
            content = it.content,
            published = it.published,
            likeOwnerIds = it.likeOwnerIds,
            likedByMe = it.likedByMe,
            attachment = it.attachment,
            likes = it.likes,
        )
    }

    fun mapPostFromDao(items: Flow<PagingData<PostEntity>>): Flow<PagingData<Post>> = items.map {
        it.map { postEntity ->
            Post(
                id = postEntity.id,
                authorId = postEntity.authorId,
                author = postEntity.author,
                authorAvatar = postEntity.authorAvatar,
                content = postEntity.content,
                published = postEntity.published,
                likeOwnerIds = postEntity.likeOwnerIds,
                likedByMe = postEntity.likedByMe,
                attachment = postEntity.attachment,
                likes = postEntity.likes,
            )
        }
    }
}