package ru.woodymsk.socialapp.data.post

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.toList
import ru.woodymsk.socialapp.data.api.PostService
import ru.woodymsk.socialapp.data.post.mapper.PostMapper
import ru.woodymsk.socialapp.data.post.model.PostDAO
import ru.woodymsk.socialapp.domain.post.PostRepository
import ru.woodymsk.socialapp.error.handler
import withContextIO
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postService: PostService,
    private val postMapper: PostMapper,
    private val postPageSource: PostPageSource,
) : PostRepository {

    companion object {
        const val PAGE_SIZE = 10
    }

    override suspend fun getAllPostList(): List<PostDAO> =
        withContextIO(handler) {
            val response = postService.getAllPostList()
            postMapper.mapToDao(response.body().orEmpty())
        }

    override suspend fun getPagedPostList(): List<PagingData<PostDAO>> = withContextIO(handler) {
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { postPageSource }
        )
        .flow
        .toList()
    }
}