package com.laanelitt.laanelittapp.profile.settings.editpassword

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentEditPasswordBinding
import com.laanelitt.laanelittapp.profile.settings.editname.EditNameViewModel
import com.laanelitt.laanelittapp.profile.settings.editname.EditNameViewModelFactory
import kotlinx.android.synthetic.main.fragment_edit_password.*

class EditPasswordFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: EditPasswordViewModel
    private lateinit var binding: FragmentEditPasswordBinding
    private lateinit var passwordCurrent: Editable
    private lateinit var passwordNew: Editable
    private lateinit var passwordConfirm: Editable
    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Henter bruker-objektet fra firebase
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_password, container, false
        )


        val application = requireNotNull(activity).application
        val viewModelFactory = EditPasswordViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditPasswordViewModel::class.java)

//        binding.editViewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner

//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)


        binding.editPasswordBtn.setOnClickListener {
            passwordCurrent = edit_password_current.editText?.text!!
            passwordNew = edit_password_new.editText?.text!!
            passwordConfirm = edit_password_confirm.editText?.text!!

            //Funsksjonen for å oppdatere passordet i firebase
            validatePassword()
        }
        return binding.root
    }

    private fun validatePassword() {
        edit_password_current.error = null
        edit_password_new.error = null
        edit_password_confirm.error = null

        //Validerer passord
        if (passwordCurrent.isEmpty()) {
            edit_password_current.error = "Alle feltene må fylles"
            edit_password_current.requestFocus()
            return
        }
        if (passwordNew.isEmpty()) {
            edit_password_new.error = "Alle feltene må fylles"
            edit_password_new.requestFocus()
            return
        }
        if (passwordConfirm.isEmpty()) {
            edit_password_confirm.error = "Alle feltene må fylles"
            edit_password_confirm.requestFocus()
            return
        }
        if (passwordNew.toString().length < 6) {
            edit_password_new.error = "Minimum 6 tegn"
            edit_password_new.requestFocus()
            return
        }
        if (passwordNew.toString() != passwordConfirm.toString()) {
            edit_password_new.error = "Ulike passord"
            edit_password_new.requestFocus()
            return
        }
        //
        viewModel.updatePassword(passwordCurrent.toString(), passwordNew.toString(), currentUser)
        //Sendes videre til innstillinger-siden
        findNavController().navigate(R.id.settingsFragment)
        Toast.makeText(
            requireContext(),
            "Oppdatert",
            Toast.LENGTH_LONG
        ).show()
    }
}