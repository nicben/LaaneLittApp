package com.laanelitt.laanelittapp.profile.settings.editzipcode

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentEditZipcodeBinding
import com.laanelitt.laanelittapp.objects.Code
import com.laanelitt.laanelittapp.objects.LocalStorage
import com.laanelitt.laanelittapp.objects.User
import com.laanelitt.laanelittapp.profile.settings.editname.EditNameViewModel
import com.laanelitt.laanelittapp.profile.settings.editname.EditNameViewModelFactory
import kotlinx.android.synthetic.main.fragment_edit_zipcode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditZipcodeFragment : Fragment() {

    private lateinit var localStorage: LocalStorage
    private lateinit var loggedInUser: User
    private lateinit var viewModel: EditZipcodeViewModel
    private lateinit var binding: FragmentEditZipcodeBinding
    private lateinit var zipcodeInput: Editable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//      Henter bruker-objektet som er lagret
        localStorage = LocalStorage(requireContext())
        loggedInUser = localStorage.getLoggedInUser!!

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_zipcode, container, false
        )
        val application = requireNotNull(activity).application
        val viewModelFactory = EditZipcodeViewModelFactory(loggedInUser, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditZipcodeViewModel::class.java)

        binding.editViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        binding.editZipcodeBtn?.setOnClickListener {
            zipcodeInput = edit_zipcode.editText?.text!!
            //Validerer inndataen og lagrer det nye postnr
            validateZipcode()
        }
        return binding.root
    }

    private fun validateZipcode() {
        edit_zipcode.error = null
        if (zipcodeInput.isEmpty()) {
            edit_zipcode.error = "Fyll inn postnummer"
            edit_zipcode.requestFocus()
            return
        }
        if (zipcodeInput.toString().length != 4) {
            //BØr sjekke om det er tall
            edit_zipcode.error = "Ugyldig postnummer"
            edit_zipcode.requestFocus()
            return
        }
        //
        viewModel.updateZipcode(
            zipcodeInput.toString(),
            loggedInUser,
            localStorage
        )
        //Sendes videre til innstillinger-siden
        findNavController().navigate(R.id.settingsFragment)
        Toast.makeText(
            requireContext(),
            "Oppdatert",
            Toast.LENGTH_LONG
        ).show()
    }
}

