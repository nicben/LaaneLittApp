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
import com.laanelitt.laanelittapp.databinding.FragmentMyAssetsListBinding
import com.laanelitt.laanelittapp.data.model.LocalStorage
import com.laanelitt.laanelittapp.utils.progressStatus
import kotlinx.android.synthetic.main.fragment_category_list.*


class MyAssetsListFragment : Fragment() {
    private lateinit var localStorage: LocalStorage
    private val viewModel: ListViewModel by lazy {
        ViewModelProvider(this).get(ListViewModel()::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        val binding = FragmentMyAssetsListBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            viewModel.displayAssetDetails(it)
        })

        //Navigerer til valgt eiendel
        viewModel.navigateToSelectedAsset.observe(viewLifecycleOwner, {
            if (null != it) {
                this.findNavController().navigate(MyAssetsListFragmentDirections.actionShowDetail(it))
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Sjekke om brurkeren er pålogget
        localStorage = LocalStorage(requireContext())
        observeAuthenticationState()

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

    private fun observeAuthenticationState() {
        val loggedInUser = localStorage.getLoggedInUser
        if (loggedInUser != null) {
            loggedInUser.id?.let {
                viewModel.getMyAssets(it)
            }
        } else {
            // Hvis brukeren ikke er logget inn blir man sendt til innloggingssiden
            findNavController().navigate(R.id.loginFragment)
        }
    }
}



