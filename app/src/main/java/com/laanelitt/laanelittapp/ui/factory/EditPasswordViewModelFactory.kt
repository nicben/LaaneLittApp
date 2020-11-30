package com.laanelitt.laanelittapp.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.laanelitt.laanelittapp.ui.viewModel.EditPasswordViewModel

//ViewModelFactory til EditPasswordViewModel
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