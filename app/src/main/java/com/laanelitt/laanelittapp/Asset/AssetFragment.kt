package com.laanelitt.laanelittapp.Asset

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.laanelitt.laanelittapp.MainActivity
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.categorylist.CategoryListFragmentArgs
import com.laanelitt.laanelittapp.databinding.FragmentAssetBinding


//profil til item - info

class AssetFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAssetBinding>(inflater,
            R.layout.fragment_asset,container,false)

       // val args = CategoryListFragmentArgs.fromBundle(requireArguments())
        val args = AssetFragmentArgs.fromBundle(requireArguments())
        Toast.makeText(context, "catId: ${args.assetId}", Toast.LENGTH_LONG).show()

            binding.btn.setOnClickListener { view : View ->
                view.findNavController().navigate(R.id.action_assetFragment_to_loanRequestPopupFragment)
            }

        return binding.root

    }

}