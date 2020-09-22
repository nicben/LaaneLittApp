package com.laanelitt.laanelittapp


//hovedside

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.laanelitt.laanelittapp.databinding.FragmentSearchPageBinding
import kotlinx.android.synthetic.main.fragment_search_page.*

class SearchPageFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentSearchPageBinding>(inflater,
            R.layout.fragment_search_page,container,false)
        binding.sokKnapp.setOnClickListener{

            //The complete onClickListener with Navigation
            binding.sokKnapp.setOnClickListener { view : View ->
                view.findNavController().navigate(R.id.action_searchPageFragment_to_searchListFragment)
            }

            binding.mineEiendelerKnapp.setOnClickListener { view : View ->
                view.findNavController().navigate(R.id.action_searchPageFragment_to_myItemListFragment)
            }
        }
        return binding.root
    }

}