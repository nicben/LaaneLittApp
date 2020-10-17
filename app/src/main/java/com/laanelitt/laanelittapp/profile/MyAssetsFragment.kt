package com.laanelitt.laanelittapp.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laanelitt.laanelittapp.objects.Asset
import com.laanelitt.laanelittapp.R


//liste over alle eiendeler


class MyAssetsFragment : Fragment() {

    private lateinit var assetList: ArrayList<Asset>
    private lateinit var linLayoutMgr: RecyclerView.LayoutManager
    private lateinit var assetAdapter: RecyclerView.Adapter<*>
    private lateinit var assetRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        assetList= Asset.makeAssetListe(resources)

        val layout= inflater.inflate(R.layout.fragment_my_assets, container, false)

        linLayoutMgr= LinearLayoutManager(context)
        assetAdapter= AssetListAdapter(context, assetList)
        assetRecyclerView=layout.findViewById<RecyclerView>(R.id.recyclerMyItemList).apply{
            setHasFixedSize(true)
            layoutManager=linLayoutMgr
            adapter=assetAdapter
        }

        return layout
        // Inflate the layout for this fragment


    }

}


