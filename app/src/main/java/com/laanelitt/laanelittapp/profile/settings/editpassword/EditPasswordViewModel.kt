package com.laanelitt.laanelittapp.profile.settings.editpassword

import android.app.Application
import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.objects.Code
import com.laanelitt.laanelittapp.objects.LocalStorage
import com.laanelitt.laanelittapp.objects.User
import kotlinx.android.synthetic.main.fragment_edit_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditPasswordViewModel(
                            app: Application) : AndroidViewModel(app) {
//    private val _loggedInUser = MutableLiveData<FirebaseUser>()
//    private val _password = MutableLiveData<String>("")
//    val password: LiveData<String>
//        get() = _password
//
//    // The external LiveData for the SelectedProperty
//    val loggedInUser: LiveData<FirebaseUser>
//        get() = _loggedInUser
//
//    // Initialize the _selectedProperty MutableLiveData
//    init {
//        _loggedInUser.value = user
//    }

//    fun onClick(view: View?) {
//        Log.d(ContentValues.TAG, "Hallloooooooooooooooooooooooooo EditUser")
//        updateName(user)
//        var text = user.value + " " + _lastname.value
//        Log.d(ContentValues.TAG, text)
//        val loginUser = LoginUser(email.getValue(), password.getValue())
//        userMutableLiveData!!.setValue(loginUser)
//    }

    fun updatePassword(oldPassword: String, newPassword: String, user: FirebaseUser){
        if (user.email != null) {
            //Bruker firebase sin re-autentisering
            val credential = EmailAuthProvider
                .getCredential(user.email!!, oldPassword)
            user.reauthenticate(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(ContentValues.TAG, "Bruker ble re-autentisert")
                    //Oppdaterer firebase objektet med firebase updatePassword()
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(ContentValues.TAG, "Passord er oppdatert")
//                                Toast.makeText(
//                                    requireContext(),
//                                    "Oppdatert",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                                //Sendes videre til innstillinger-siden
//                                findNavController().navigate(R.id.settingsFragment)
                            } else {
                                Log.d(ContentValues.TAG, "Passord ble ikke oppdatert")
//                                Toast.makeText(
//                                    requireContext(), "Noe gikk galt",
//                                    Toast.LENGTH_LONG
//                                ).show()
                            }
                        }

                } else {
                    Log.d(ContentValues.TAG, "Bruker ble ikke re-autentisert")
//                    edit_password_current.error  = "Feil passord"
                }
            }
        }
    }

}