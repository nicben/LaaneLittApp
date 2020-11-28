package com.laanelitt.laanelittapp.profile.myassets


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.ListViewModel
import com.laanelitt.laanelittapp.PhotoGridAdapter
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentMyAssetsListBinding
import com.laanelitt.laanelittapp.objects.LocalStorage


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
        viewModel.response.observe(viewLifecycleOwner, {
            if(it==viewModel.status[1]){
                Toast.makeText(context, "Feilet, prøver på nytt", Toast.LENGTH_LONG).show()
            }else if(it==viewModel.status[2]){
                Toast.makeText(context, "Noe gikk galt", Toast.LENGTH_LONG).show()
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



