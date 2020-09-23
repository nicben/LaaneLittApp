package com.laanelitt.laanelittapp.searchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.laanelitt.laanelittapp.R

//liste etter søk
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    /*private val viewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }*/

    private lateinit var assetList: ArrayList<Asset>
    private lateinit var linLayoutMgr: RecyclerView.LayoutManager
    private lateinit var assetAdapter: RecyclerView.Adapter<*>
    private lateinit var assetRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        assetList=Asset.makeAssetListe(resources)

        val layout= inflater.inflate(R.layout.fragment_search_list, container, false)

        linLayoutMgr=LinearLayoutManager(context)
        assetAdapter=AssetListAdapter(context, assetList)
        assetRecyclerView=layout.findViewById<RecyclerView>(R.id.recyclerSearchList).apply{
            setHasFixedSize(true)
            layoutManager=linLayoutMgr
            adapter=assetAdapter
        }/**/

        return layout
        // Inflate the layout for this fragment
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}