package com.laanelitt.laanelittapp.categorylist

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

//liste etter søk
import androidx.recyclerview.widget.LinearLayoutManager
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentCategoryListBinding
import com.laanelitt.laanelittapp.login.LoginFragment
import com.laanelitt.laanelittapp.login.LoginFragmentArgs
import com.laanelitt.laanelittapp.login.LoginFragmentDirections

class CategoryListFragment : Fragment() {
    /**/
    private val viewModel: CategoryViewModel by lazy {
        ViewModelProviders.of(this).get(CategoryViewModel()::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        println("****************************************AHHHH")
        val binding=FragmentCategoryListBinding.inflate(inflater)

        binding.setLifecycleOwner(this)
        binding.viewModel=viewModel

        binding.recyclerCategoryList.adapter= CategoryListAdapter()
        /*binding.recyclerCategoryList.adapter= CategoryListAdapter(CategoryListAdapter.OnClickListener{
            viewModel.displayAsset(it)
        })

        viewModel.navigateToSelectedAsset.observe(this, Observer {

        })*/

        binding.recyclerCategoryList.layoutManager=LinearLayoutManager(context)

        println("****************************************AHHHH2")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = CategoryListFragmentArgs.fromBundle(requireArguments())
        Toast.makeText(context, "catId: ${args.catId}", Toast.LENGTH_LONG).show()

        // If the user presses the back button, bring them back to the home screen.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.searchPageFragment, false)
        }

    }
}