package ru.woodymsk.socialapp.data.post

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams.Append
import androidx.paging.PagingSource.LoadParams.Prepend
import androidx.paging.PagingSource.LoadParams.Refresh
import androidx.paging.PagingSource.LoadResult.Error
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.woodymsk.socialapp.data.api.PostService
import ru.woodymsk.socialapp.data.post.mapper.PostMapper
import ru.woodymsk.socialapp.data.post.model.PostDAO
import withContextIO
import javax.inject.Inject

class PostPageSource @Inject constructor(
    private val postService: PostService,
    private val postMapper: PostMapper,
) : PagingSource<Int, PostDAO>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostDAO> =
        withContextIO {

            try {
                val response = when (params) {
                    is Refresh -> postService.getLatest(params.loadSize)
                    is Append -> {
                        postService.getBeforePost(
                            params.key.toString(),
                            params.loadSize
                        )
                    }

                    is Prepend -> {
                        return@withContextIO Page(
                            data = emptyList(),
                            prevKey = params.key,
                            nextKey = null,
                        )
                    }
                }

                val postList = response.body() ?: throw HttpException(response)

                val key = postList.lastOrNull()?.id

                return@withContextIO Page(
                    data = postMapper.mapToDao(postList),
                    prevKey = params.key,
                    nextKey = key,
                )
            } catch (e: Exception) {
                return@withContextIO Error(e)
            }
        }

    override fun getRefreshKey(state: PagingState<Int, PostDAO>): Int? = null
}