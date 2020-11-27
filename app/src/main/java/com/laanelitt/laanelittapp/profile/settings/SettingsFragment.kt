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
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var localStorage: LocalStorage

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

        //
        auth = FirebaseAuth.getInstance()
        localStorage = LocalStorage(requireContext())

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
        //Logg ut-knapp
        binding.settingLogoutButton.setOnClickListener {
            //Fjerner den lagrede dataen
            localStorage.clearUserData()
            //Firebase sin logg ut-funksjon
            auth.signOut()
            findNavController().navigate(R.id.loginFragment)
        }
    }
}