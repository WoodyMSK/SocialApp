package ru.woodymsk.socialapp.domain.post.mapper

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.woodymsk.socialapp.domain.post.model.Post
import ru.woodymsk.socialapp.data.post.model.PostEntity
import javax.inject.Inject

class PostMapper @Inject constructor() {

    fun mapPostsFromEntity(items: Flow<PagingData<PostEntity>>): Flow<PagingData<Post>> = items.map {
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
                ownedByMe = postEntity.ownedByMe,
            )
        }
    }

    fun mapSinglePostToEntity(item: Post): PostEntity = PostEntity(
        id = item.id,
        authorId = item.authorId,
        author = item.author,
        authorAvatar = item.authorAvatar,
        content = item.content,
        published = item.published,
        likeOwnerIds = item.likeOwnerIds,
        likedByMe = item.likedByMe,
        attachment = item.attachment,
        likes = item.likes,
        ownedByMe = item.ownedByMe,
    )
}