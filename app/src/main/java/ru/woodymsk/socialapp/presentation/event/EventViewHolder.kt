package ru.woodymsk.socialapp.presentation.event

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import ru.woodymsk.socialapp.R.drawable.ic_profile_24
import ru.woodymsk.socialapp.data.model.AttachmentType.IMAGE
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.databinding.ItemCardEventBinding
import ru.woodymsk.socialapp.domain.load
import ru.woodymsk.socialapp.domain.parseAndFormatDate

class EventViewHolder(
    private val binding: ItemCardEventBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(event: Event) {
        binding.apply {
            tvCardEventAuthor.text = event.author
            tvCardEventPublished.text = parseAndFormatDate(event.published)
            tvCardEventEventDate.text = parseAndFormatDate(event.datetime)
            tvCardEventEventFormat.text = event.type.toString()
            tvCardEventParticipantCount.text = event.speakerIds.size.toString()
            tvCardEventDescription.text = event.content
            if (event.content.length > 200) {
                bCardEventMoreContentText.isVisible = true
            } else {
                bCardEventMoreContentText.isVisible = false
            }
            bCardEventMoreContentText.setOnClickListener {
                tvCardEventDescription.maxLines = event.content.length
                bCardEventMoreContentText.isVisible = false
            }
            if (event.authorAvatar != null) {
                ivCardEventAuthorAvatar.load(event.authorAvatar, CircleCrop())
            } else {
                ivCardEventAuthorAvatar.setImageResource(ic_profile_24)
            }
            if (event.attachment != null && event.attachment.type == IMAGE) {
                ivCardEventPicture.load(event.attachment.url)
                ivCardEventPicture.isVisible = true
            } else {
                ivCardEventPicture.isVisible = false
            }
        }
    }
}