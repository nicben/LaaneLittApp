package com.laanelitt.laanelittapp.ui.viewModel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.laanelitt.laanelittapp.utils.progressStatus


class EditPasswordViewModel(app: Application) : AndroidViewModel(app) {
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    fun updatePassword(oldPassword: String, newPassword: String, user: FirebaseUser){
        if (user.email != null) {
            _response.value = progressStatus[0]
            //Bruker firebase sin re-autentisering
            //https://firebase.google.com/docs/auth/android/manage-users
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
                                _response.value = progressStatus[1]
                            } else {
                                Log.d(ContentValues.TAG, "Passord ble ikke oppdatert")
                                _response.value = progressStatus[2]
                            }
                        }
                } else {
                    Log.d(ContentValues.TAG, "Bruker ble ikke re-autentisert")
                    _response.value = progressStatus[5]
                }
            }
        }
    }

}