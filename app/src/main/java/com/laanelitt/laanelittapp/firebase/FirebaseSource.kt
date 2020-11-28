package com.laanelitt.laanelittapp.firebase

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.login.NewUserFragmentDirections
import com.laanelitt.laanelittapp.objects.Code
import com.laanelitt.laanelittapp.objects.LocalStorage
import com.laanelitt.laanelittapp.objects.LoggedInUser
import com.laanelitt.laanelittapp.objects.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirebaseSource {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun firebaseAuth(username: String, password: String, localStorage: LocalStorage) {
        //Firebase
        firebaseAuth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    println("firebaseAuth: suksess")
                    login(
                        username,
                        password,
                        localStorage
                    )
                } else {
                    println("firebaseAuth:feilet"+ task.exception)
                }
            }
    }

    private fun login(username: String, password: String,  localStorage: LocalStorage) {
        //ApiService
        LaneLittApi.retrofitService.login(username, password).enqueue(
            object : Callback<LoggedInUser> {
                override fun onResponse(
                    call: Call<LoggedInUser>,
                    response: Response<LoggedInUser>) {
                    println("login: onResponse " + response.body()?.user?.id.toString())
                    //Henter bruker-objektet
                    val user = response.body()?.user
                    //Lagrer bruker-objektet og setter userLoggedIn til true
                    localStorage.storeUserData(user!!)
                    localStorage.setUserLoggedIn(true)
                }
                //Man kommer rett hit ved feil brukernavn/passord
                override fun onFailure(call: Call<LoggedInUser>, t: Throwable) {
                    println("login: onFailure$t")
                }
            }
        )
    }

    fun firebaseSignIn(firstname: String, lastname: String, username: String, password: String) {
        //Firebase
        firebaseAuth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "firebaseSignIn: suksess")
                    //Funsksjon fra ApiService for å registrere brukeren i databasen
                    register(
                        firstname,
                        lastname,
                        username,
                        password,
                    )
                } else {
                    Log.w(ContentValues.TAG, "firebaseSignIn:feilet", task.exception)
                }
            }
    }

    private fun register(firstname: String, lastname: String, username: String, password1: String) {
        //Oppretter et nytt bruker-objekt
        val newUser = User(null, firstname, "", lastname,"user", null,  username, password1, "", true)
        //registerUser fra ApiService
        LaneLittApi.retrofitService.registerUser(newUser).enqueue(
            object : Callback<Code> {
                override fun onResponse(call: Call<Code>, response: Response<Code>) {
                    if (response.body()?.code.toString() == "200") {
                        Log.d(ContentValues.TAG, "register: suksess " + response.body()?.toString())
                        //Sender brukernavn og passord til LoginFragment
//                        findNavController().navigate(
//                            NewUserFragmentDirections.actionNewUserFragmentToLoginFragment(
//                                username,
//                                password1
//                            )
//                        )
                    } else {
                        Log.d(ContentValues.TAG, "register: feilet " + response.body()?.toString())

                    }
                }

                override fun onFailure(call: Call<Code>, t: Throwable) {
                    Log.d(ContentValues.TAG, "register: onFailure$t")
                }
            }
        )
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser
}