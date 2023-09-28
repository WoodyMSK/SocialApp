package ru.woodymsk.socialapp.presentation.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import ru.woodymsk.socialapp.databinding.ItemLoadStateBinding

class PagingLoadStateAdapter(
    private val onRetryListener: () -> Unit,
) : LoadStateAdapter<PagingLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingLoadStateViewHolder {
        return PagingLoadStateViewHolder(
            ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onRetryListener,
        )
    }

    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}