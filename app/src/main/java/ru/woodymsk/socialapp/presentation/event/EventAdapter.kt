package ru.woodymsk.socialapp.presentation.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.woodymsk.socialapp.domain.post.model.EventsItem
import ru.woodymsk.socialapp.databinding.ItemCardEventBinding

class EventAdapter : ListAdapter<EventsItem, EventViewHolder>(EventDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            ItemCardEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class EventDiffCallBack : DiffUtil.ItemCallback<EventsItem>() {
    override fun areItemsTheSame(oldItem: EventsItem, newItem: EventsItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EventsItem, newItem: EventsItem): Boolean {
        return oldItem == newItem
    }
}