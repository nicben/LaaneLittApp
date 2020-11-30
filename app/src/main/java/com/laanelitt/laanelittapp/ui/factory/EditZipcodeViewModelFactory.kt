package com.laanelitt.laanelittapp.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.laanelitt.laanelittapp.data.model.User
import com.laanelitt.laanelittapp.ui.viewModel.EditZipcodeViewModel

class EditZipcodeViewModelFactory (
    private val user: User,
    private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditZipcodeViewModel::class.java)) {
                return EditZipcodeViewModel(user, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}