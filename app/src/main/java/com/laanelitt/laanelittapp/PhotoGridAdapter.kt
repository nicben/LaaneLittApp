/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.laanelitt.laanelittapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laanelitt.laanelittapp.databinding.GridViewItemBinding
import com.laanelitt.laanelittapp.objects.Asset


class PhotoGridAdapter( private val onClickListener: OnClickListener) :
        ListAdapter<Asset,
                PhotoGridAdapter.AssetViewHolder>(DiffCallback) {

    class AssetViewHolder(private var binding: GridViewItemBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(asset: Asset) {
            binding.property = asset
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
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

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): AssetViewHolder {
        return AssetViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        val asset = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(asset)
        }
        holder.bind(asset)
    }


    class OnClickListener(val clickListener: (asset:Asset) -> Unit) {
        fun onClick(asset:Asset) = clickListener(asset)
    }

}

