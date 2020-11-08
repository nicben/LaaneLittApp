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
import com.laanelitt.laanelittapp.objects.Assets

class CategoryListAdapter() : ListAdapter<Assets, CategoryListAdapter.CategoryViewHolder>(DiffCallback){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding: CatAssetCardBinding = CatAssetCardBinding.inflate(LayoutInflater.from(parent.context))
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val assets=getItem(position)
        val imgUri="https://lanelitt.no/AssetImages/"+assets.AssetImages[0].imageUrl
        val uri=imgUri.toUri().buildUpon().scheme("https").build()
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(CategoryListFragmentDirections.actionCategoryListFragmentToAssetFragment(assets.id.toString()))
        }
        Glide.with(holder.binding.catAssetImage.context).load(uri).into(holder.binding.catAssetImage)
        holder.bind(assets)
    }
    companion object DiffCallback: DiffUtil.ItemCallback<Assets>() {
        override fun areItemsTheSame(oldItem: Assets, newItem: Assets): Boolean {
            return  oldItem===newItem
        }

        override fun areContentsTheSame(oldItem: Assets, newItem: Assets): Boolean {
            return oldItem.assetName==newItem.assetName
        }

    }
    class CategoryViewHolder(var binding: CatAssetCardBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(assets: Assets){
            binding.assets=assets
            binding.executePendingBindings()
        }
    }



}