package com.laanelitt.laanelittapp.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentSettingsBinding
import com.laanelitt.laanelittapp.login.LoginFragment


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // If the user presses the back button, bring them back to the home screen.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.searchPageFragment, false)
        }

        binding.logoutBtn.setOnClickListener {
            LoginFragment.Pref.setUserId(requireContext(), "ID", "")
            //LoginFragment.Pref.removeUserId(requireContext(), "ID", "null")
            findNavController().navigate(R.id.loginFragment)
        }

    }

}