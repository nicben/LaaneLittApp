package com.laanelitt.laanelittapp.profile.settings.editimage

import android.Manifest
import android.app.Application
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.laanelitt.laanelittapp.objects.User


class EditImageViewModel(user: User,
                         app: Application) : AndroidViewModel(app) {
    private val _loggedInUser = MutableLiveData<User>()

    // The external LiveData for the SelectedProperty
    val loggedInUser: LiveData<User>
        get() = _loggedInUser

    // Initialize the _selectedProperty MutableLiveData
    init {
        _loggedInUser.value = user
    }

}