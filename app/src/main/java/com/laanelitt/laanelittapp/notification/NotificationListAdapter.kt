package com.laanelitt.laanelittapp.notification


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laanelitt.laanelittapp.databinding.NotificationGridViewItemBinding
import com.laanelitt.laanelittapp.objects.Notification

class NotificationListAdapter(private val onClickListener: OnClickListener):
    ListAdapter<Notification,
            NotificationListAdapter.NotificationViewHolder>(DiffCallback) {

    class NotificationViewHolder(private var binding: NotificationGridViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {
            binding.notification = notification
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
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

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
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
    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(notification)
        }
        holder.bind(notification)
    }

    class OnClickListener(val clickListener: (notification:Notification) -> Unit) {
        fun onClick(notification:Notification) = clickListener(notification)
    }
}