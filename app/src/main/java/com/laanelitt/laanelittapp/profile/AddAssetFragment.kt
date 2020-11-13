package com.laanelitt.laanelittapp.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentAddAssetBinding
import com.laanelitt.laanelittapp.login.LoginFragment


class AddAssetFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (LoginFragment.Pref.getUserId(requireContext(), "ID", "null") == "") {
            findNavController().navigate(R.id.loginFragment)}
       val binding = DataBindingUtil.inflate<FragmentAddAssetBinding>(inflater,R.layout.fragment_add_asset,container,false)

        binding.saveButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_addAssetFragment_to_myAssetsFragment)
        }

        return binding.root

    }

}