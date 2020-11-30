package com.laanelitt.laanelittapp.ui.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.ui.viewModel.ListViewModel
import com.laanelitt.laanelittapp.ui.adapter.PhotoGridAdapter
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentSearchListBinding
import com.laanelitt.laanelittapp.data.model.LocalStorage
import com.laanelitt.laanelittapp.utils.progressStatus
import kotlinx.android.synthetic.main.fragment_category_list.*

class SearchListFragment : Fragment() {
    private lateinit var localStorage: LocalStorage
    private val viewModel: ListViewModel by lazy {
        ViewModelProvider(this).get(ListViewModel()::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        localStorage = LocalStorage(requireContext())

        val binding = FragmentSearchListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //Kaller på getAssetSearch-funksjonen fra ListViewModel
        val loggedInUser = localStorage.getLoggedInUser
        viewModel.getAssetSearch(loggedInUser?.id!!,
            SearchListFragmentArgs.fromBundle(requireArguments()).searchtext)
        //Kobler til PhotoGridAdapter
        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            viewModel.displayAssetDetails(it)
        })

        //Går til valgt eiendel
        viewModel.navigateToSelectedAsset.observe(viewLifecycleOwner, {
            if ( null != it ) {
                this.findNavController().navigate(SearchListFragmentDirections.actionShowDetail(it))
                viewModel.displayAssetDetailsComplete()
            }
        })

        //Observerer om det er endringer i viewModelen, viser progressbar og tar seg av API feil
        viewModel.response.observe(viewLifecycleOwner, {
            if (it == progressStatus[0]){
                progressbar.visibility = View.VISIBLE
            }
            else if (it == progressStatus[1]){
                progressbar.visibility = View.GONE
            }
            else if (it == progressStatus[3]){
                progressbar.visibility = View.GONE
                Toast.makeText(context, "Noe gikk galt",Toast.LENGTH_LONG).show()
                this.findNavController().navigate(R.id.homePageFragment)
            }
        })
        return binding.root
    }//end onCreateView
}