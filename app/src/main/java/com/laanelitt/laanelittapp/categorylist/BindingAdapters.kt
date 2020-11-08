package com.laanelitt.laanelittapp.categorylist

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.laanelitt.laanelittapp.objects.Asset


/*@BindingAdapter("imageUrl")
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
}*/
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asset>?) {
    println("****************BindingAdapter************************")
    val adapter = recyclerView.adapter as CategoryListAdapter
    adapter.submitList(data)
}

