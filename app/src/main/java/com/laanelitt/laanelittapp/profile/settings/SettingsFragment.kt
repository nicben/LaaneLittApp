package com.laanelitt.laanelittapp.profile.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentSettingsBinding
import com.laanelitt.laanelittapp.objects.LocalStorage


class SettingsFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    var localStorage: LocalStorage? = null
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        localStorage = LocalStorage(requireContext())

        binding.settingNameButton.setOnClickListener {

            findNavController().navigate(R.id.editNameFragment)
        }

        binding.settingImageButton.setOnClickListener {

            findNavController().navigate(R.id.editImageFragment)
        }

        binding.settingPasswordButton.setOnClickListener {

            findNavController().navigate(R.id.editPasswordFragment)
        }

        binding.settingLogoutButton.setOnClickListener {
            localStorage!!.clearUserData()
            auth.signOut()
            findNavController().navigate(R.id.loginFragment)
        }

    }

}