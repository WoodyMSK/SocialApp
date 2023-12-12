package ru.woodymsk.socialapp.presentation.post

import android.annotation.SuppressLint
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.R.drawable.ic_profile_24
import ru.woodymsk.socialapp.data.model.AttachmentType.IMAGE
import ru.woodymsk.socialapp.domain.post.model.Post
import ru.woodymsk.socialapp.databinding.ItemCardPostBinding
import ru.woodymsk.socialapp.domain.load
import ru.woodymsk.socialapp.domain.orFalse
import ru.woodymsk.socialapp.domain.orZero
import ru.woodymsk.socialapp.domain.parseAndFormatDate
import ru.woodymsk.socialapp.presentation.post.model.PostPayload

class PostViewHolder(
    private val binding: ItemCardPostBinding,
    private val onClickListener: PostClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ResourceType")
    fun bind(payload: PostPayload) {
        payload.liked?.let { liked ->
            binding.bCardPostLike.text = payload.likedByMe.toString()
            like(liked)
        }
        binding.bCardPostLike.setOnClickListener {
            onClickListener.onLike(payload.id.orZero(), payload.liked.orFalse())
        }
    }

    @SuppressLint("ResourceType")
    fun bind(post: Post) {
        binding.apply {
            tvCardPostAuthor.text = post.author
            tvCardPostPublished.text = parseAndFormatDate(post.published)
            tvCardPostText.text = post.content
            like(post.likedByMe)
            bCardPostMoreContentText.isVisible = post.content.length > 200
            bCardPostLike.text = post.likes.toString()
            bCardPostMoreContentText.setOnClickListener {
                tvCardPostText.maxLines = post.content.length
                bCardPostMoreContentText.isVisible = false
            }
            bCardPostLike.text = post.likes.toString()
            if (post.authorAvatar != null) {
                ivCardPostAuthorAvatar.load(post.authorAvatar, CircleCrop())
            } else {
                ivCardPostAuthorAvatar.setImageResource(ic_profile_24)
            }
            if (post.attachment != null && post.attachment.type == IMAGE) {
                ivCardPostPicture.load(post.attachment.url)
                ivCardPostPicture.isVisible = true
            } else {
                ivCardPostPicture.isVisible = false
            }
            bCardPostLike.setOnClickListener {
                onClickListener.onLike(post.id, post.likedByMe)
            }
            bCardPostMenu.isVisible = post.ownedByMe
            bCardPostMenu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_options)
                    menu.setGroupVisible(R.id.menuPost, post.ownedByMe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.bEdit -> {
                                onClickListener.onEdit(post)
                                true
                            }
                            R.id.bDelete -> {
                                onClickListener.onDelete(post.id)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }

    private fun like(likedByMe: Boolean) {
        if (likedByMe) {
            binding.bCardPostLike.setIconResource(R.drawable.ic_like_filled_24dp)
            binding.bCardPostLike.setIconTintResource(R.color.red)
        } else {
            binding.bCardPostLike.setIconResource(R.drawable.ic_like_outlined_24)
            binding.bCardPostLike.setIconTintResource(R.color.gray)
        }
    }
}