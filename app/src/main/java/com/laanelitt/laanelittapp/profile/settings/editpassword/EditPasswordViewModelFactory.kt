package com.laanelitt.laanelittapp.profile.settings.editpassword

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.laanelitt.laanelittapp.objects.User

class EditPasswordViewModelFactory (
    private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditPasswordViewModel::class.java)) {
                return EditPasswordViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}