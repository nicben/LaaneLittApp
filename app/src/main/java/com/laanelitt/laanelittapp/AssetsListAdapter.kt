package com.laanelitt.laanelittapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.laanelitt.laanelittapp.databinding.AssetCardBinding
import com.laanelitt.laanelittapp.objects.Assets

class AssetsListAdapter : ListAdapter<Assets, AssetsListAdapter.AssetsViewHolder>(DiffCallback){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):AssetsViewHolder{
        val binding:AssetCardBinding=AssetCardBinding.inflate(LayoutInflater.from(parent.context))
        return AssetsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AssetsViewHolder, position: Int) {
        val assets=getItem(position)
        val imgUri="https://lanelitt.no/AssetImages/"+assets.assetImages[0].imageUrl
        val uri=imgUri.toUri().buildUpon().scheme("https").build()

        Glide.with(holder.binding.assetImage.context).load(uri).into(holder.binding.assetImage)
        holder.bind(assets)
    }
    class AssetsViewHolder(var binding: AssetCardBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(assets: Assets){
            binding.assets=assets
            binding.executePendingBindings()
        }
    }
    companion object DiffCallback: DiffUtil.ItemCallback<Assets>() {
        override fun areItemsTheSame(oldItem: Assets, newItem: Assets): Boolean {
            return  oldItem===newItem
        }

        override fun areContentsTheSame(oldItem: Assets, newItem: Assets): Boolean {
            return oldItem.assetName==newItem.assetName
        }

    }

}