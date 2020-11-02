package com.laanelitt.laanelittapp.categorylist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

//liste etter søk
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentCategoryListBinding

class CategoryListFragment : Fragment() {
    /**/
    private val viewModel: CategoryListViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CategoryListViewModel()::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding=FragmentCategoryListBinding.inflate(inflater)
        println("****************************************AHHHH  ")

        viewModel.getCatAssets(CategoryListFragmentArgs.fromBundle(requireArguments()).catId)
        println("****************************************AHHHH1,5  ")

        binding.setLifecycleOwner(this)
        binding.viewModel=viewModel

        binding.recyclerCategoryList.adapter= CategoryListAdapter()

        binding.recyclerCategoryList.layoutManager = GridLayoutManager(context, 2)

        println("****************************************AHHHH2  ")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("****************************************viewCreated")
        super.onViewCreated(view, savedInstanceState)

        val args = CategoryListFragmentArgs.fromBundle(requireArguments())
        Toast.makeText(context, "catId: ${args.catId}", Toast.LENGTH_LONG).show()

        // If the user presses the back button, bring them back to the home screen.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.searchPageFragment, false)
        }

    }
}