package com.laanelitt.laanelittapp.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentSettingsBinding
import com.laanelitt.laanelittapp.data.model.LocalStorage
import com.laanelitt.laanelittapp.ui.factory.AssetViewModelFactory
import com.laanelitt.laanelittapp.ui.viewModel.AssetViewModel
import com.laanelitt.laanelittapp.ui.viewModel.FirebaseViewModel
import com.laanelitt.laanelittapp.utils.observeAuthenticationState


class SettingsFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var localStorage: LocalStorage
    private lateinit var viewModel: FirebaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        localStorage = LocalStorage(requireContext())
        viewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        observeAuthenticationState(localStorage, this)

        //Endre navn-knapp
        binding.settingNameButton.setOnClickListener {
            findNavController().navigate(R.id.editNameFragment)
        }
        //Endre passord-knapp
        binding.settingPasswordButton.setOnClickListener {
            findNavController().navigate(R.id.editPasswordFragment)
        }
        //Endre profilbilde-knapp
        binding.settingImageButton.setOnClickListener {
            findNavController().navigate(R.id.editImageFragment)
        }
        //Endre postnr-knapp
        binding.settingZipcodeButton?.setOnClickListener {
            findNavController().navigate(R.id.editZipcodeFragment)
        }
        binding.settingAboutButton.setOnClickListener{
            val uri = Uri.parse("https://github.com/nicben/LaaneLittApp")
            val webIntent = Intent(Intent.ACTION_VIEW, uri)
            if (webIntent.resolveActivity(requireActivity().packageManager) != null) startActivity(webIntent)
        }
        //Logg ut-knapp
        binding.settingLogoutButton.setOnClickListener {
            viewModel.signOut(auth, localStorage)
            findNavController().navigate(R.id.loginFragment)
        }
    }
}