package com.laanelitt.laanelittapp.searchpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentSearchPageBinding
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.login.LoginFragment.Pref.getUserId

class SearchPageFragment : Fragment() {

    private lateinit var binding: FragmentSearchPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search_page, container, false
        )
        //The complete onClickListener with Navigation
        binding.verktoyKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(SearchPageFragmentDirections.actionSearchPageFragmentToCategoryListFragment("1"))
        }

        binding.elKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(SearchPageFragmentDirections.actionSearchPageFragmentToCategoryListFragment("2") )
        }

        binding.instrumentKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(SearchPageFragmentDirections.actionSearchPageFragmentToCategoryListFragment("3"))
        }

        binding.klerKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(SearchPageFragmentDirections.actionSearchPageFragmentToCategoryListFragment(
                     "6"))
        }

        binding.skoKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(SearchPageFragmentDirections.actionSearchPageFragmentToCategoryListFragment(
                    "7"))
        }

        binding.bokerKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(SearchPageFragmentDirections.actionSearchPageFragmentToCategoryListFragment(
                    "8"))
        }

        binding.sportKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(SearchPageFragmentDirections.actionSearchPageFragmentToCategoryListFragment(
                    "9"))
        }

        binding.multiKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(SearchPageFragmentDirections.actionSearchPageFragmentToCategoryListFragment(
                    "10"))
        }

        binding.divKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(SearchPageFragmentDirections.actionSearchPageFragmentToCategoryListFragment(
                    "11"))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()
    }

    fun observeAuthenticationState() {

        if (getUserId(requireContext(), "ID", "null") != "") {
            binding.idText.text = getUserId(requireContext(), "ID", "null")

        } else {
            // Hvis brukeren ikke er logget inn blir man sendt til innloggingssiden
            findNavController().navigate(R.id.loginFragment)
        }
    }
}