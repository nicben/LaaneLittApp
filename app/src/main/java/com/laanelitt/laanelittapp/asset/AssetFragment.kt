package com.laanelitt.laanelittapp.asset

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.laanelitt.laanelittapp.databinding.FragmentAssetBinding


//profil til item - info

class AssetFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application
        val binding = FragmentAssetBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val asset = AssetFragmentArgs.fromBundle(requireArguments()).selectedProperty
        val viewModelFactory = AssetViewModelFactory(asset, application)
        binding.viewModel = ViewModelProvider(
            this, viewModelFactory).get(AssetViewModel::class.java)

        // val args = CategoryListFragmentArgs.fromBundle(requireArguments())
/* val args = AssetFragmentArgs.fromBundle(requireArguments())
     Toast.makeText(context, "catId: ${args.assetId}", Toast.LENGTH_LONG).show()

     binding.btn.tOnClickListener { view : View ->
         view.findNavController().navigate(R.id.action_assetFragment_to_loanRequestPopupFragment)
     }*/

 return binding.root

}

}