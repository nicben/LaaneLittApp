package com.laanelitt.laanelittapp.searchlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.laanelitt.laanelittapp.objects.OldAsset
import com.laanelitt.laanelittapp.MainActivity
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.searchlist.SearchListAdapter.AssetViewHolder

class SearchListAdapter(private val context: Context?, var oldAssetList: ArrayList<OldAsset>):RecyclerView.Adapter<AssetViewHolder>(){
    private val assetsList: ArrayList<OldAsset>

    //private val mInflater: LayoutInflater
    init {
        //mInflater= LayoutInflater.from(context)
        assetsList=oldAssetList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val mInflater= LayoutInflater.from(parent.context)
        val myItemView=mInflater.inflate(R.layout.asset_card, parent, false)
        return  AssetViewHolder(myItemView, this)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {

        val thisAsset=assetsList[position]
        holder.assetNameView.text="søk "+thisAsset.assetName

        val imgUri=thisAsset.imageLink.toUri().buildUpon().scheme("https").build()

        Glide.with(holder.assetImageView.context).load(imgUri).into(holder.assetImageView)
    }

    override fun getItemCount(): Int =assetsList.size


    inner class AssetViewHolder(itemView: View, adapter: SearchListAdapter):RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val assetNameView: TextView
        val assetImageView: ImageView

        val assAdapter: SearchListAdapter
        init {
            assetNameView=itemView.findViewById<View>(R.id.assetName) as TextView
            assetImageView=itemView.findViewById<View>(R.id.assetImage) as ImageView
            assAdapter=adapter
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val asset = assetsList[layoutPosition]
            //view?.findNavController()?.navigate(R.id.action_categoryListFragment_to_assetFragment)
            MainActivity.visSnackbar(view, "Du valgte " + asset.assetName)
        }
    }


}
