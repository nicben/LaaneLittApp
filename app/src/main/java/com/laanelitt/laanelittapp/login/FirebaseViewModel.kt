package com.laanelitt.laanelittapp.login

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.objects.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FirebaseViewModel(app: Application) : AndroidViewModel(app) {


fun firebaseAuth(
    username: String,
    password: String,
    firebaseAuth: FirebaseAuth,
    localStorage: LocalStorage
) {
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
                    println("firebaseAuth:feilet" + task.exception)
                }
            }
    }

    private fun login(username: String, password: String, localStorage: LocalStorage) {
        //ApiService
        LaneLittApi.retrofitService.login(username, password).enqueue(
            object : Callback<LoggedInUser> {
                override fun onResponse(
                    call: Call<LoggedInUser>,
                    response: Response<LoggedInUser>
                ) {
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

    fun firebaseSignIn(
        firstname: String,
        lastname: String,
        username: String,
        password: String,
        firebaseAuth: FirebaseAuth
    ) {
        //Firebase
        firebaseAuth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("firebaseSignIn: suksess")
                    //Funsksjon fra ApiService for å registrere brukeren i databasen
                    register(
                        firstname,
                        lastname,
                        username,
                        password
                    )
                } else {
                    println("firebaseSignIn:feilet" + task.exception)
                }
            }
    }

    private fun register(firstname: String, lastname: String, username: String, password1: String) {
        //Oppretter et nytt bruker-objekt
        val newUser = User(
            null,
            firstname,
            "",
            lastname,
            "user",
            null,
            username,
            password1,
            "",
            true
        )
        //registerUser fra ApiService
        LaneLittApi.retrofitService.registerUser(newUser).enqueue(
            object : Callback<Code> {
                override fun onResponse(call: Call<Code>, response: Response<Code>) {
                    println("register: onResponse " + response.body()?.toString())

                }

                override fun onFailure(call: Call<Code>, t: Throwable) {
                    println("register: onFailure$t")
                }
            }
        )
    }
}

