package ru.woodymsk.socialapp.presentation.post_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.woodymsk.socialapp.data.dto.PostDTO
import ru.woodymsk.socialapp.databinding.ItemCardPostBinding

class PostAdapter : ListAdapter<PostDTO, PostViewHolder>(PostDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            ItemCardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PostDiffCallBack : DiffUtil.ItemCallback<PostDTO>() {
    override fun areItemsTheSame(oldItem: PostDTO, newItem: PostDTO): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PostDTO, newItem: PostDTO): Boolean {
        return oldItem == newItem
    }
}