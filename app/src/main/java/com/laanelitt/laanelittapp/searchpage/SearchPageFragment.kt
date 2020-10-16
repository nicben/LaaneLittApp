package com.laanelitt.laanelittapp.searchpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentSearchPageBinding

import androidx.fragment.app.viewModels
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()

    }

    private fun observeAuthenticationState() {

        if (getUserId(requireContext(), "ID", "null") == "Logget inn") {
            binding.idText.text = getUserId(requireContext(), "ID", "null")

        } else {
            // Hvis brukeren ikke er logget inn blir man sendt til innloggingssiden
            findNavController().navigate(R.id.loginFragment)
        }
    }
}