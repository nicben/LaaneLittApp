package com.laanelitt.laanelittapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laanelitt.laanelittapp.databinding.NotificationGridViewItemBinding
import com.laanelitt.laanelittapp.data.model.Notification

//Liste adapter for RecycleViewet til NotificationListen
class NotificationListAdapter(private val onClickListener: OnClickListener):
    ListAdapter<Notification,
            NotificationListAdapter.NotificationViewHolder>(DiffCallback) {

    class NotificationViewHolder(private var binding: NotificationGridViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {
            binding.notification = notification
            binding.executePendingBindings()
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            NotificationGridViewItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(notification)
        }
        holder.bind(notification)
    }

    class OnClickListener(val clickListener: (notification: Notification) -> Unit) {
        fun onClick(notification: Notification) = clickListener(notification)
    }
}