package ru.woodymsk.socialapp.presentation.common

import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter

fun <T : Any> PagingDataAdapter<T, *>.addPagingAdapterLoadStateListener(handleError: (Throwable) -> Unit) =
    this.addLoadStateListener { loadState ->
        val errorState = when {
            loadState.append is LoadState.Error -> loadState.append as LoadState.Error
            loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
            loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
            else -> null
        }

        errorState?.let {
            handleError(it.error)
        }
    }