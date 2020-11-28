package com.laanelitt.laanelittapp.profile.settings.editname

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.laanelitt.laanelittapp.objects.User

class EditNameViewModelFactory (
    private val user: User,
    private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditNameViewModel::class.java)) {
                return EditNameViewModel(user, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}