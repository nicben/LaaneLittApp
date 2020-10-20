package com.laanelitt.laanelittapp.categorylist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

//liste etter søk
import androidx.recyclerview.widget.LinearLayoutManager
import com.laanelitt.laanelittapp.databinding.FragmentCategoryListBinding

class CategoryFragment : Fragment() {
    /**/
    private val viewModel: CategoryViewModel by lazy {
        ViewModelProviders.of(this).get(CategoryViewModel()::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        println("****************************************AHHHH")
        val binding=FragmentCategoryListBinding.inflate(inflater)

        binding.setLifecycleOwner(this)
        binding.viewModel=viewModel

        binding.recyclerCategoryList.adapter= CategoryListAdapter()
        /*binding.recyclerCategoryList.adapter= CategoryListAdapter(CategoryListAdapter.OnClickListener{
            viewModel.displayAsset(it)
        })

        viewModel.navigateToSelectedAsset.observe(this, Observer {

        })*/

        binding.recyclerCategoryList.layoutManager=LinearLayoutManager(context)

        println("****************************************AHHHH2")

        return binding.root
    }
}