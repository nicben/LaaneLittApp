package com.laanelitt.laanelittapp.profile.myassets


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.ListViewModel
import com.laanelitt.laanelittapp.PhotoGridAdapter
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentMyAssetsListBinding
import com.laanelitt.laanelittapp.objects.LocalStorage


class MyAssetsListFragment : Fragment() {

    private var localStorage: LocalStorage? = null

    private val viewModel: ListViewModel by lazy {
        ViewModelProvider(this).get(ListViewModel()::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        val binding= FragmentMyAssetsListBinding.inflate(inflater)

        localStorage = LocalStorage(requireContext())

        observeAuthenticationState()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })

        // Observe the navigateToSelectedProperty LiveData and Navigate when it isn't null
        // After navigating, call displayPropertyDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, {
            if (null != it) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(MyAssetsListFragmentDirections.actionShowDetail(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayPropertyDetailsComplete()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("***************** myAsset viewCreated *************************")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){
        inflater.inflate(R.menu.action_bar, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> findNavController().navigate(R.id.settingsFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    fun observeAuthenticationState() {

        val loggedInUser = localStorage?.getLoggedInUser
        if (loggedInUser != null) {
            loggedInUser.id?.let {
                viewModel.getMyAssets(it)
            }
            println("*********************   my assts, userId: " + loggedInUser.id + " *******************")

        } else {
            // Hvis brukeren ikke er logget inn blir man sendt til innloggingssiden
            findNavController().navigate(R.id.loginFragment)
        }
    }
}


/* private lateinit var oldAssetList: ArrayList<OldAsset>
private lateinit var linLayoutMgr: RecyclerView.LayoutManager
private lateinit var assetAdapter: RecyclerView.Adapter<*>
private lateinit var assetRecyclerView: RecyclerView

override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    if (LoginFragment.Pref.getUserId(requireContext(), "ID", "null") == "") {
    findNavController().navigate(R.id.loginFragment)}

    oldAssetList= OldAsset.makeAssetListe(resources)

    val layout= inflater.inflate(R.layout.fragment_my_assets_list, container, false)

    linLayoutMgr= LinearLayoutManager(context)
    linLayoutMgr= GridLayoutManager(context, 2)
    assetAdapter= AssetListAdapter(context, oldAssetList)
    assetRecyclerView=layout.findViewById<RecyclerView>(R.id.recyclerMyItemList).apply{
        setHasFixedSize(true)
        layoutManager=linLayoutMgr
        adapter=assetAdapter
    }


    return layout
    // Inflate the layout for this fragment

}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // If the user presses the back button, bring them back to the home screen.
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        findNavController().popBackStack(R.id.searchPageFragment, false)
    }

    settings_btn.setOnClickListener {
        findNavController().navigate(R.id.settingsFragment)
    }

}

}*/


