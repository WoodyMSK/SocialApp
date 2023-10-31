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
        )
    }

    fun mapPostFromDao(items: Flow<PagingData<PostEntity>>): Flow<PagingData<Post>> = items.map {
        it.map { postDAO ->
            Post(
                id = postDAO.id,
                authorId = postDAO.authorId,
                author = postDAO.author,
                authorAvatar = postDAO.authorAvatar,
                content = postDAO.content,
                published = postDAO.published,
                likeOwnerIds = postDAO.likeOwnerIds,
                likedByMe = postDAO.likedByMe,
                attachment = postDAO.attachment,
            )
        }
    }
}