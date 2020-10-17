package com.laanelitt.laanelittapp.searchlist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

//liste etter søk
import androidx.recyclerview.widget.LinearLayoutManager
import com.laanelitt.laanelittapp.AssetViewModel
import com.laanelitt.laanelittapp.AssetsListAdapter
import com.laanelitt.laanelittapp.databinding.FragmentSearchListBinding

class SearchListFragment : Fragment() {
    /**/
    private val viewModel:AssetViewModel by lazy {
        ViewModelProviders.of(this).get(AssetViewModel::class.java)
    }
    /*
    private lateinit var assetList: ArrayList<Asset>
    private lateinit var linLayoutMgr: RecyclerView.LayoutManager
    private lateinit var assetAdapter: RecyclerView.Adapter<*>
    private lateinit var assetRecyclerView: RecyclerView*/



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        println("****************************************AHHHH")
        val binding=FragmentSearchListBinding.inflate(inflater)

        binding.setLifecycleOwner(this)
        binding.viewModel=viewModel

        binding.recyclerSearchList.adapter= AssetsListAdapter()
        binding.recyclerSearchList.layoutManager=LinearLayoutManager(context)
        //setHasOptionsMenu(true)
        println("****************************************AHHHH2")

        return binding.root

        /*
        assetList=Asset.makeAssetListe(resources)

        val layout= inflater.inflate(R.layout.fragment_search_list, container, false)

        linLayoutMgr=LinearLayoutManager(context)
        assetAdapter= SearchListAdapter(context, assetList)
        assetRecyclerView=layout.findViewById<RecyclerView>(R.id.recyclerSearchList).apply{
            setHasFixedSize(true)
            layoutManager=linLayoutMgr
            adapter=assetAdapter
        }

        return layout*/
        // Inflate the layout for this fragment
    }



}