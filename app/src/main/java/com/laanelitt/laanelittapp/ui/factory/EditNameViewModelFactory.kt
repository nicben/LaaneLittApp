package com.laanelitt.laanelittapp.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.laanelitt.laanelittapp.data.model.User
import com.laanelitt.laanelittapp.ui.viewModel.EditNameViewModel

//ViewModelFactory til EdditNameViewModel
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