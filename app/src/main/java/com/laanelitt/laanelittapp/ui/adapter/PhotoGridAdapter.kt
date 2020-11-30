package com.laanelitt.laanelittapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laanelitt.laanelittapp.databinding.GridViewItemBinding
import com.laanelitt.laanelittapp.data.model.Asset

//Liste adapter for RecycleViewet til AssetListene(SearchList, MyAssetsList, CategoryList)
class PhotoGridAdapter( private val onClickListener: OnClickListener) :
        ListAdapter<Asset,
                PhotoGridAdapter.AssetViewHolder>(DiffCallback) {

    class AssetViewHolder(private var binding: GridViewItemBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(asset: Asset) {
            binding.property = asset
            binding.executePendingBindings()
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<Asset>() {
        override fun areItemsTheSame(oldItem: Asset, newItem: Asset): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Asset, newItem: Asset): Boolean {
            return oldItem.id == newItem.id
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): AssetViewHolder {
        return AssetViewHolder(
            GridViewItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }
    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        val asset = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(asset)
        }
        holder.bind(asset)
    }


    class OnClickListener(val clickListener: (asset: Asset) -> Unit) {
        fun onClick(asset: Asset) = clickListener(asset)
    }

}

