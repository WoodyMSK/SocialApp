package ru.woodymsk.socialapp.presentation.post_screen

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import ru.woodymsk.socialapp.R.drawable.ic_profile_24
import ru.woodymsk.socialapp.data.dto.PostDTO
import ru.woodymsk.socialapp.databinding.ItemCardPostBinding
import ru.woodymsk.socialapp.domain.load
import ru.woodymsk.socialapp.domain.model.AttachmentTypeDAO.IMAGE
import ru.woodymsk.socialapp.domain.parseAndFormatDate

class PostViewHolder(
    private val binding: ItemCardPostBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: PostDTO) {
        binding.apply {
            tvCardPostAuthor.text = post.author
            tvCardPostPublished.text = parseAndFormatDate(post.published)
            tvCardPostText.text = post.content
            bCardPostLike.isCheckable = post.likedByMe
            if (post.content.length > 200) {
                bCardPostMoreContentText.isVisible = true
            } else {
                bCardPostMoreContentText.isVisible = false
            }
            bCardPostMoreContentText.setOnClickListener {
                tvCardPostText.maxLines = post.content.length
                bCardPostMoreContentText.isVisible = false
            }
            bCardPostLike.text = if (post.likeOwnerIds.isNotEmpty()) {
                post.likeOwnerIds.size.toString()
            } else null
            if (post.authorAvatar != null) {
                ivCardPostAuthorAvatar.load(post.authorAvatar, CircleCrop())
            } else {
                ivCardPostAuthorAvatar.setImageResource(ic_profile_24)
            }
            if (post.attachment != null && post.attachment.type == IMAGE.name) {
                ivCardPostPicture.load(post.attachment.url)
                ivCardPostPicture.isVisible = true
            } else {
                ivCardPostPicture.isVisible = false
            }
        }
    }
}