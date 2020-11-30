package com.laanelitt.laanelittapp.ui.view

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentEditNameBinding
import com.laanelitt.laanelittapp.data.model.LocalStorage
import com.laanelitt.laanelittapp.data.model.User
import com.laanelitt.laanelittapp.ui.viewModel.EditNameViewModel
import com.laanelitt.laanelittapp.ui.factory.EditNameViewModelFactory
import com.laanelitt.laanelittapp.utils.progressStatus
import kotlinx.android.synthetic.main.fragment_edit_name.*

class EditNameFragment : Fragment() {
    private lateinit var localStorage: LocalStorage
    private lateinit var loggedInUser: User
    private lateinit var viewModel: EditNameViewModel
    private lateinit var binding: FragmentEditNameBinding
    private lateinit var firstnameInput: Editable
    private lateinit var lastnameInput: Editable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Henter bruker-objektet som er lagret
        localStorage = LocalStorage(requireContext())
        loggedInUser = localStorage.getLoggedInUser!!

        //Kobler til FragmentEditNameBinding, EditNameViewModelFactory og EditNameViewModel
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_name, container, false
        )
        val application = requireNotNull(activity).application
        val viewModelFactory = EditNameViewModelFactory(loggedInUser, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditNameViewModel::class.java)
        binding.editViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.response.observe(viewLifecycleOwner, Observer{
            if ( it == progressStatus[1]) {
                //Sendes videre til innstillinger-siden ved suksess
                this. findNavController().navigate(R.id.settingsFragment)
                Toast.makeText(
                    requireContext(),
                    "Oppdatert",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        binding.editNameBtn.setOnClickListener {
            //Henter tekstene far tekstfeltene
            firstnameInput = edit_firstname.editText?.text!!
            lastnameInput = edit_lastname.editText?.text!!

            //Validerer inndataene og lagrer det nye navnet
            validateName()
        }
        return binding.root
    }

    private fun validateName() {
        edit_firstname.error = null
        edit_lastname.error = null
        if (firstnameInput.isEmpty()) {
            edit_firstname.error = "Fyll inn fornavn"
            edit_firstname.requestFocus()
            return
        }
        if (lastnameInput.isEmpty()) {
            edit_lastname.error = "Fyll inn etternavn"
            edit_lastname.requestFocus()
            return
        }else {
            //
            viewModel.updateName(
                loggedInUser.id!!,
                firstnameInput.toString(),
                lastnameInput.toString(),
                loggedInUser,
                localStorage
            )
        }
    }
}
