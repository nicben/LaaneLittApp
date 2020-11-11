package com.laanelitt.laanelittapp.categorylist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.Observer
import com.laanelitt.laanelittapp.ListViewModel
import com.laanelitt.laanelittapp.PhotoGridAdapter
import com.laanelitt.laanelittapp.databinding.FragmentCategoryListBinding

//liste etter kategori
class CategoryListFragment : Fragment() {
    /**/
    private val viewModel: ListViewModel by lazy {
        ViewModelProvider(this).get(ListViewModel()::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding=FragmentCategoryListBinding.inflate(inflater)
        println("****************************************AHHHH  ")

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.getCatAssets(CategoryListFragmentArgs.fromBundle(requireArguments()).catId)

        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })

        // Observe the navigateToSelectedProperty LiveData and Navigate when it isn't null
        // After navigating, call displayPropertyDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(CategoryListFragmentDirections.actionShowDetail(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayPropertyDetailsComplete()
            }
        })


        println("****************************************AHHHH2  ")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("****************************************viewCreated")
        super.onViewCreated(view, savedInstanceState)

        /*val args = CategoryListFragmentArgs.fromBundle(requireArguments())
        Toast.makeText(context, "catId: ${args.catId}", Toast.LENGTH_LONG).show()

        // If the user presses the back button, bring them back to the home screen.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.searchPageFragment, false)
        }*/

    }
}