package com.laanelitt.laanelittapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.laanelitt.laanelittapp.SearchListAdapter.AssetViewHolder

class SearchListAdapter(private val context: Context, var assetList: ArrayList<Asset>):RecyclerView.Adapter<AssetViewHolder>(){
    private val assetsList: ArrayList<Asset>
    private val mInflater: LayoutInflater
    init {
        mInflater= LayoutInflater.from(context)
        assetsList=assetList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val myItemView=mInflater.inflate(R.layout.asset_card, parent, false)
        return  AssetViewHolder(myItemView, this)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        val thisAsset=assetsList[position]
        holder.assetNameView.text=thisAsset.assetName

    }

    override fun getItemCount(): Int =assetsList.size


    inner class AssetViewHolder(itemView: View, adapter: SearchListAdapter):RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val assetNameView: TextView
        val assAdapter:SearchListAdapter
        init {
            assetNameView=itemView.findViewById<View>(R.id.assetName) as TextView
            assAdapter=adapter
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val asset = assetsList[layoutPosition]
            MainActivity.visSnackbar(view, "Du valgte "+ asset.assetName)
        }
    }
}