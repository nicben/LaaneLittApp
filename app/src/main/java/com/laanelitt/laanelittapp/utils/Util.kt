package com.laanelitt.laanelittapp.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.data.model.LocalStorage


fun observeAuthenticationState(localStorage: LocalStorage, fragment: Fragment) {
    val loggedInUser = localStorage.getLoggedInUser
    if (loggedInUser == null) {
        // Hvis brukeren ikke er logget inn blir man sendt til innloggingssiden
        findNavController(fragment).navigate(R.id.loginFragment)
    }
}//end observeAuthenticationState

val progressStatus = arrayOf("Working", "Succsess", "Failure", "Critical failure", "timeout", "AuthFailure")