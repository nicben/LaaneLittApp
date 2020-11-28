package com.laanelitt.laanelittapp.firebase

import com.laanelitt.laanelittapp.objects.LocalStorage

class UserRepository (
    private val firebase: FirebaseSource
){
    fun login(email: String, password: String, localStorage: LocalStorage) = firebase.firebaseAuth(email, password, localStorage)

    fun register(firstname: String, lastname: String, username: String, password: String) = firebase.firebaseSignIn(firstname, lastname, username, password)

    fun currentUser() = firebase.currentUser()

    fun logout() = firebase.logout()
}