package ru.woodymsk.socialapp.data.post

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingSource.LoadResult.Error
import androidx.paging.PagingState
import ru.woodymsk.socialapp.data.api.PostService
import ru.woodymsk.socialapp.data.post.mapper.PostMapper
import ru.woodymsk.socialapp.data.post.model.PostDAO
import ru.woodymsk.socialapp.domain.orZero
import ru.woodymsk.socialapp.error.AppError
import ru.woodymsk.socialapp.error.handler
import withContextIO
import javax.inject.Inject

class PostPageSource @Inject constructor(
    private val postService: PostService,
    private val postMapper: PostMapper,
) : PagingSource<Int, PostDAO>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostDAO> =
        withContextIO(handler) {

            val pageNumber = params.key.orZero()
            val pageSize: Int = params.loadSize
            val response = postService.getBeforePost(pageNumber.toString(), pageSize)

            return@withContextIO if (response.isSuccessful) {
                val postList = postMapper.mapToDao(response.body().orEmpty())
                val nextPageNumber = if (postList.isEmpty()) null else pageNumber.inc()
                val prevPageNumber = if (pageNumber > 1) pageNumber.dec() else null
                Page(postList, prevPageNumber, nextPageNumber)
            } else {
                Error(AppError.handleError(AppError.NetworkError))
            }
        }

    override fun getRefreshKey(state: PagingState<Int, PostDAO>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }
}