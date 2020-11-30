package com.laanelitt.laanelittapp.ui.adapter

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.data.model.Asset
import com.laanelitt.laanelittapp.data.model.Notification



@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asset>?) {
    println("****************BindingAdapter************************")
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}

@BindingAdapter("notificationListData")
fun bindNotificationRecyclerView(recyclerView: RecyclerView, data: List<Notification>?) {
    println("****************BindingAdapter************************")
    val adapter = recyclerView.adapter as NotificationListAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val image = "https://lanelitt.no/AssetImages/" + imgUrl
        val imgUri = image.toUri().buildUpon().scheme("https").build()

        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}
@BindingAdapter("ownerImageUrl")
fun bindOwnerImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val image = "https://lanelitt.no/profileImages/" + imgUrl
        val imgUri = image.toUri().buildUpon().scheme("https").build()

        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}


