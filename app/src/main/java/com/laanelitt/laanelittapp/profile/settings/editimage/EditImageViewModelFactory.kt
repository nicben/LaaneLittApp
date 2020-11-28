package com.laanelitt.laanelittapp.profile.settings.editimage

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.laanelitt.laanelittapp.objects.User

class EditImageViewModelFactory (
    private val user: User,
    private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditImageViewModel::class.java)) {
                return EditImageViewModel(user, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}