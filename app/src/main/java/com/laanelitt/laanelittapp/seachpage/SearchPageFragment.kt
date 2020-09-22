package com.laanelitt.laanelittapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.database.ItemDatabase
import com.laanelitt.laanelittapp.databinding.FragmentSearchPageBinding

class SearchPageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentSearchPageBinding>(
            inflater, R.layout.fragment_search_page, container, false)


        //The complete onClickListener with Navigation
        binding.verktoyKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_searchPageFragment_to_searchListFragment)
        }

        binding.elKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_searchPageFragment_to_searchListFragment)
        }

        binding.instrumentKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_searchPageFragment_to_searchListFragment)
        }

        binding.klerKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_searchPageFragment_to_searchListFragment)
        }

        binding.skoKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_searchPageFragment_to_searchListFragment)
        }

        binding.bokerKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_searchPageFragment_to_searchListFragment)
        }


        binding.sportKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_searchPageFragment_to_searchListFragment)
        }

        binding.multiKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_searchPageFragment_to_searchListFragment)
        }

        binding.divKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_searchPageFragment_to_searchListFragment)
        }


        return binding.root
    }




}