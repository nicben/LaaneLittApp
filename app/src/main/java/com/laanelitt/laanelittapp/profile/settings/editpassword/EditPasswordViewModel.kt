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


class EditPasswordViewModel(app: Application) : AndroidViewModel(app) {
    val status = arrayOf("Success", "Failure", "AuthFailure")
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

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
                                _response.value = status[0]
                            } else {
                                Log.d(ContentValues.TAG, "Passord ble ikke oppdatert")
                                _response.value = status[1]
                            }
                        }
                } else {
                    Log.d(ContentValues.TAG, "Bruker ble ikke re-autentisert")
                    _response.value = status[2]
                }
            }
        }
    }

}