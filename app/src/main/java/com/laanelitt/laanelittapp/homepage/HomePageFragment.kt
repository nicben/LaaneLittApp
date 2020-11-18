package com.laanelitt.laanelittapp.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentHomePageBinding
import com.laanelitt.laanelittapp.login.LoginFragment.Pref.getUserId

class HomePageFragment : Fragment(){

    private lateinit var binding: FragmentHomePageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home_page, container, false
        )

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                findNavController().navigate(
                   HomePageFragmentDirections.actionSearchPageFragmentToSearchListFragment(query
                    )
                )
                //Toast.makeText(context, query, Toast.LENGTH_LONG).show()
                return false
            }

        })

        //The complete onClickListener with Navigation
        binding.verktoyKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(
                    HomePageFragmentDirections.actionSearchPageFragmentToCategoryListFragment(
                        "1"
                    )
                )
        }

        binding.elKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(
                    HomePageFragmentDirections.actionSearchPageFragmentToCategoryListFragment(
                        "2"
                    )
                )
        }

        binding.instrumentKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(
                    HomePageFragmentDirections.actionSearchPageFragmentToCategoryListFragment(
                        "3"
                    )
                )
        }

        binding.klerKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(
                    HomePageFragmentDirections.actionSearchPageFragmentToCategoryListFragment(
                        "6"
                    )
                )
        }

        binding.skoKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(
                    HomePageFragmentDirections.actionSearchPageFragmentToCategoryListFragment(
                        "7"
                    )
                )
        }

        binding.bokerKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(
                    HomePageFragmentDirections.actionSearchPageFragmentToCategoryListFragment(
                        "8"
                    )
                )
        }

        binding.sportKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(
                    HomePageFragmentDirections.actionSearchPageFragmentToCategoryListFragment(
                        "9"
                    )
                )
        }

        binding.multiKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(
                    HomePageFragmentDirections.actionSearchPageFragmentToCategoryListFragment(
                        "10"
                    )
                )
        }

        binding.divKnapp.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(
                    HomePageFragmentDirections.actionSearchPageFragmentToCategoryListFragment(
                        "11"
                    )
                )
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
            //findNavController().navigate(R.id.notificationsFragment)

        } else {
            // Hvis brukeren ikke er logget inn blir man sendt til innloggingssiden
            findNavController().navigate(R.id.loginFragment)
        }
    }


}


