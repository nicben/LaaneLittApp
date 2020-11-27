package com.laanelitt.laanelittapp.profile.settings

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
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentEditPasswordBinding
import com.laanelitt.laanelittapp.objects.LocalStorage
import kotlinx.android.synthetic.main.fragment_edit_password.*
import kotlinx.android.synthetic.main.fragment_new_user.*

class EditPasswordFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentEditPasswordBinding
    private lateinit var passwordCurrent: Editable
    private lateinit var passwordNew: Editable
    private lateinit var passwordConfirm: Editable
    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_password, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Henter bruker-objektet fra firebase
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

        binding.editPasswordBtn.setOnClickListener {
            passwordCurrent = edit_password_current.editText?.text!!
            passwordNew = edit_password_new.editText?.text!!
            passwordConfirm = edit_password_confirm.editText?.text!!

            //Funsksjonen for å oppdatere passordet i firebase
            validatePassword()
        }
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
        updatePassword(passwordCurrent.toString(), passwordNew.toString(), currentUser)
    }

    private fun updatePassword(oldPassword: String, newPassword: String, user: FirebaseUser){

        if (user.email != null) {
            //Bruker firebase sin re-autentisering
            val credential = EmailAuthProvider
                .getCredential(user.email!!, oldPassword)
            user.reauthenticate(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "Bruker ble re-autentisert")
                    //Oppdaterer firebase objektet med firebase updatePassword()
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "Passord er oppdatert")
                                Toast.makeText(
                                    requireContext(),
                                    "Oppdatert",
                                    Toast.LENGTH_LONG
                                ).show()
                                //Sendes videre til innstillinger-siden
                                findNavController().navigate(R.id.settingsFragment)
                            } else {
                                Log.d(TAG, "Passord ble ikke oppdatert")
                                Toast.makeText(
                                    requireContext(), "Noe gikk galt",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                } else {
                    Log.d(TAG, "Bruker ble ikke re-autentisert")
                    edit_password_current.error  = "Feil passord"
                }
            }
        }
    }
}