package com.laanelitt.laanelittapp.ui.view

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentEditZipcodeBinding
import com.laanelitt.laanelittapp.data.model.LocalStorage
import com.laanelitt.laanelittapp.data.model.User
import com.laanelitt.laanelittapp.ui.viewModel.EditZipcodeViewModel
import com.laanelitt.laanelittapp.ui.factory.EditZipcodeViewModelFactory
import com.laanelitt.laanelittapp.utils.progressStatus
import kotlinx.android.synthetic.main.fragment_edit_zipcode.*

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

        //Henter bruker-objektet som er lagret
        localStorage = LocalStorage(requireContext())
        loggedInUser = localStorage.getLoggedInUser!!

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_zipcode, container, false
        )
        val application = requireNotNull(activity).application
        val viewModelFactory = EditZipcodeViewModelFactory(loggedInUser, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditZipcodeViewModel::class.java)
        binding.editViewModel = viewModel
        binding.lifecycleOwner = this

        binding.editZipcodeBtn?.setOnClickListener {
            zipcodeInput = edit_zipcode.editText?.text!!
            //Validerer inndataen og lagrer det nye postnr
            validateZipcode()
        }

        viewModel.response.observe(viewLifecycleOwner, Observer{
            if ( it == viewModel.status[1]) {
                //Sendes videre til innstillinger-siden ved suksess
                this.findNavController().navigate(R.id.settingsFragment)
                Toast.makeText(
                    requireContext(),
                    "Oppdatert",
                    Toast.LENGTH_LONG
                ).show()
            }else if (it == progressStatus[2]){
                edit_zipcode.error = "Ugyldig postnummer"
            }
        })

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
            edit_zipcode.error = "Ugyldig postnummer"
            edit_zipcode.requestFocus()
            return
        }else {
            //
            viewModel.updateZipcode(
                zipcodeInput.toString(),
                loggedInUser,
                localStorage
            )
        }
    }
}

