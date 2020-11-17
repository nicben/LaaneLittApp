package com.laanelitt.laanelittapp.profile.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentEditImageBinding
import com.laanelitt.laanelittapp.databinding.FragmentEditNameBinding
import com.laanelitt.laanelittapp.databinding.FragmentSettingsBinding
import com.laanelitt.laanelittapp.login.LoginFragment

class EditImageFragment : Fragment() {


    private lateinit var binding: FragmentEditImageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_image, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}