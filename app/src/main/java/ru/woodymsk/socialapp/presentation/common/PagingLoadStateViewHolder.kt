package ru.woodymsk.socialapp.presentation.common

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import ru.woodymsk.socialapp.databinding.ItemLoadStateBinding

class PagingLoadStateViewHolder(
    private val binding: ItemLoadStateBinding,
    private val onRetryListener: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(state: LoadState) = with(binding) {
        prLoadState.isVisible = state is LoadState.Loading
        etErrorLoadState.isVisible = state is LoadState.Error
        etTryAgainLoadState.isVisible = state is LoadState.Error
        etTryAgainLoadState.setOnClickListener { onRetryListener() }
    }
}