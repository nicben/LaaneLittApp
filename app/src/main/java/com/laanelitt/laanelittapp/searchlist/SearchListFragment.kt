package com.laanelitt.laanelittapp.searchlist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.Observer
import com.laanelitt.laanelittapp.ListViewModel
import com.laanelitt.laanelittapp.PhotoGridAdapter
import com.laanelitt.laanelittapp.databinding.FragmentSearchListBinding
import com.laanelitt.laanelittapp.objects.LocalStorage

class SearchListFragment : Fragment() {
    var localStorage: LocalStorage? = null

    private val viewModel: ListViewModel by lazy {
        ViewModelProvider(this).get(ListViewModel()::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding= FragmentSearchListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        localStorage = LocalStorage(requireContext())

        val loggedInUser = localStorage?.getLoggedInUser
        if (loggedInUser != null) {
            loggedInUser.id?.let { viewModel.getAssetSearch(it, SearchListFragmentArgs.fromBundle(requireArguments()).searchtext) }
            println("*********************   searchlist, userId: " + loggedInUser.id + " *******************")
        }

        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })

        // Observe the navigateToSelectedProperty LiveData and Navigate when it isn't null
        // After navigating, call displayPropertyDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(SearchListFragmentDirections.actionShowDetail(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayPropertyDetailsComplete()
            }
        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("************************ SearchList viewCreated *************************")
        super.onViewCreated(view, savedInstanceState)
    }
}