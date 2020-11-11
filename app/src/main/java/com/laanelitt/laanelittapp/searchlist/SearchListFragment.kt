package com.laanelitt.laanelittapp.searchlist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.Observer
import com.laanelitt.laanelittapp.ListViewModel
import com.laanelitt.laanelittapp.PhotoGridAdapter
import com.laanelitt.laanelittapp.categorylist.CategoryListFragmentArgs
import com.laanelitt.laanelittapp.databinding.FragmentCategoryListBinding
import com.laanelitt.laanelittapp.databinding.FragmentSearchListBinding
import com.laanelitt.laanelittapp.login.LoginFragment

//liste etter kategori
class SearchListFragment : Fragment() {
    /**/
    private val viewModel: ListViewModel by lazy {
        ViewModelProvider(this).get(ListViewModel()::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding= FragmentSearchListBinding.inflate(inflater)
        println("****************************************AHHHH  ")

        binding.lifecycleOwner = this

        binding.viewModel = viewModel



        val userId = LoginFragment.Pref.getUserId(requireContext(), "ID", "null")
        if (userId != null) {
            viewModel.getAssetSearch(userId, SearchListFragmentArgs.fromBundle(requireArguments()).searchtext)
            println("*********************   searchlist, userId: " + userId + " *******************")
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

        println("****************************************AHHHH2  ")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("************************ SearchList viewCreated *************************")
        super.onViewCreated(view, savedInstanceState)

        /*val args = CategoryListFragmentArgs.fromBundle(requireArguments())
        Toast.makeText(context, "catId: ${args.catId}", Toast.LENGTH_LONG).show()

        // If the user presses the back button, bring them back to the home screen.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.searchPageFragment, false)
        }*/

    }
}