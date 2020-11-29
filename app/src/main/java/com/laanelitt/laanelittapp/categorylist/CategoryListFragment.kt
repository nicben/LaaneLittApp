package com.laanelitt.laanelittapp.categorylist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.ListViewModel
import com.laanelitt.laanelittapp.PhotoGridAdapter
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentCategoryListBinding
import com.laanelitt.laanelittapp.progressStatus
import kotlinx.android.synthetic.main.fragment_category_list.*


class CategoryListFragment : Fragment() {
    private lateinit var binding: FragmentCategoryListBinding
    private val viewModel: ListViewModel by lazy {
        ViewModelProvider(this).get(ListViewModel()::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_category_list,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.getCatAssets(CategoryListFragmentArgs.fromBundle(requireArguments()).catId)

        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })

        // Observe the navigateToSelectedProperty LiveData and Navigate when it isn't null
        // After navigating, call displayPropertyDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, {
            if ( null != it ) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(CategoryListFragmentDirections.actionShowDetail(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayPropertyDetailsComplete()
            }
        })
        viewModel.response.observe(viewLifecycleOwner, {
            if (it == progressStatus[0]){
                progressbar.visibility = View.VISIBLE
            }
            else if (it == progressStatus[1]){
                progressbar.visibility = View.GONE
            }
            else if (it == progressStatus[2]){
                //Toast.makeText(context, "Feilet, prøver på nytt",Toast.LENGTH_LONG).show()
            }
            else if (it == progressStatus[3]){
                progressbar.visibility = View.GONE
                Toast.makeText(context, "Noe gikk galt",Toast.LENGTH_LONG).show()
                this.findNavController().navigate(R.id.homePageFragment)
            }
        })



        return binding.root
    }
}