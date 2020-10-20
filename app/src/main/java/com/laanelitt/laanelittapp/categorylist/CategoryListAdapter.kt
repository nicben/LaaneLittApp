package com.laanelitt.laanelittapp.categorylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.laanelitt.laanelittapp.MainActivity
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.AssetCardBinding
import com.laanelitt.laanelittapp.objects.Assets

class CategoryListAdapter(/*private val onClickListener: OnClickListener*/) : ListAdapter<Assets, CategoryListAdapter.CategoryViewHolder>(DiffCallback){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding:AssetCardBinding=AssetCardBinding.inflate(LayoutInflater.from(parent.context))
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val assets=getItem(position)
        val imgUri="https://lanelitt.no/AssetImages/"+assets.assetImages[0].imageUrl
        val uri=imgUri.toUri().buildUpon().scheme("https").build()
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.action_categoryListFragment_to_assetFragment)
            MainActivity.visSnackbar(it, "Du valgte "+assets.assetName+". ID: "+assets.id)
        }
        /*holder.itemView.setOnClickListener {
            onClickListener.onClick(assets)
        }*/
        Glide.with(holder.binding.assetImage.context).load(uri).into(holder.binding.assetImage)
        holder.bind(assets)
    }
    class OnClickListener(val clickListener: (assets:Assets) -> Unit) {
        fun onClick(assets:Assets) = clickListener(assets)
    }
    companion object DiffCallback: DiffUtil.ItemCallback<Assets>() {
        override fun areItemsTheSame(oldItem: Assets, newItem: Assets): Boolean {
            return  oldItem===newItem
        }

        override fun areContentsTheSame(oldItem: Assets, newItem: Assets): Boolean {
            return oldItem.assetName==newItem.assetName
        }

    }
    class CategoryViewHolder(var binding: AssetCardBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(assets: Assets){
            binding.assets=assets
            binding.executePendingBindings()
        }
        /*
        override fun onClick(view: View?) {
            val assets = assetsList[layoutPosition]
            view?.findNavController()?.navigate(R.id.action_myAssetsFragment_to_assetFragment)
            MainActivity.visSnackbar(view, "Du valgte " + assets.assetName)
        }*/
    }

}