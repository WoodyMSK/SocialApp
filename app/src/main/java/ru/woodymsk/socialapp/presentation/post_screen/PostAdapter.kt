package ru.woodymsk.socialapp.presentation.post_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.woodymsk.socialapp.data.model.PostsItem
import ru.woodymsk.socialapp.databinding.ItemCardPostBinding

class PostAdapter : ListAdapter<PostsItem, PostViewHolder>(PostDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            ItemCardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PostDiffCallBack : DiffUtil.ItemCallback<PostsItem>() {
    override fun areItemsTheSame(oldItem: PostsItem, newItem: PostsItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PostsItem, newItem: PostsItem): Boolean {
        return oldItem == newItem
    }
}