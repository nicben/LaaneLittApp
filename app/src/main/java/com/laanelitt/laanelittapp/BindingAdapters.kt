package com.laanelitt.laanelittapp

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.laanelitt.laanelittapp.objects.Assets


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?){
    println("****************bindImage************************")
    imgUrl?.let {
        val imgUri=imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
                .load(imgUri)
                .apply(RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                )
                .into(imgView)
    }
}
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Assets>?) {
    println("****************BindingAdapter************************")
    val adapter = recyclerView.adapter as AssetsListAdapter
    adapter.submitList(data)
}

