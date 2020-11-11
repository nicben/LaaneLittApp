package com.laanelitt.laanelittapp.categorylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.laanelitt.laanelittapp.databinding.CatAssetCardBinding
import com.laanelitt.laanelittapp.objects.Asset

class CategoryListAdapter() : ListAdapter<Asset, CategoryListAdapter.CategoryViewHolder>(DiffCallback){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding: CatAssetCardBinding = CatAssetCardBinding.inflate(LayoutInflater.from(parent.context))
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val assets=getItem(position)
        val imgUri="https://lanelitt.no/AssetImages/"+assets.assetImages[0].imageUrl
        val uri=imgUri.toUri().buildUpon().scheme("https").build()
        holder.itemView.setOnClickListener {
        }
        holder.bind(assets)
    }
    companion object DiffCallback: DiffUtil.ItemCallback<Asset>() {
        override fun areItemsTheSame(oldItem: Asset, newItem: Asset): Boolean {
            return  oldItem===newItem
        }

        override fun areContentsTheSame(oldItem: Asset, newItem: Asset): Boolean {
            return oldItem.assetName==newItem.assetName
        }

    }
    class CategoryViewHolder(var binding: CatAssetCardBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(assets: Asset){
            binding.assets=assets
            binding.executePendingBindings()
        }
    }
}