package com.laanelitt.laanelittapp.ui.viewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.laanelitt.laanelittapp.data.api.LaneLittApi
import com.laanelitt.laanelittapp.data.model.Code
import com.laanelitt.laanelittapp.data.model.LocalStorage
import com.laanelitt.laanelittapp.data.model.LoggedInUser
import com.laanelitt.laanelittapp.data.model.User
import com.laanelitt.laanelittapp.objects.NewUser
import com.laanelitt.laanelittapp.utils.progressStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FirebaseViewModel(app: Application) : AndroidViewModel(app) {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    fun firebaseLogin(
        username: String,
        password: String,
        firebaseAuth: FirebaseAuth,
        localStorage: LocalStorage
    ) {
        //Firebase
        //https://firebase.google.com/docs/auth/android/password-auth
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
                    Log.d(TAG, "firebaseAuth:feilet" + task.exception)
                    _response.value = progressStatus[5]
                }
            }
    }

    private fun login(username: String, password: String, localStorage: LocalStorage) {
        _response.value = progressStatus[0]
        //ApiService
        LaneLittApi.retrofitService.login(username, password).enqueue(
            object : Callback<LoggedInUser> {
                override fun onResponse(
                    call: Call<LoggedInUser>,
                    response: Response<LoggedInUser>) {
                    _response.value = progressStatus[1]
                    //Henter bruker-objektet
                    val user = response.body()?.user
                    //Lagrer bruker-objektet og setter userLoggedIn til true
                    localStorage.storeUserData(user!!)
                    localStorage.setUserLoggedIn(true)
                }
                //Man kommer rett hit ved feil brukernavn/passord
                override fun onFailure(call: Call<LoggedInUser>, t: Throwable) {
                    if(t.message == progressStatus[4]) {
                        Log.d(TAG, "login: onFailure ${t.message}")
                        login(username, password,localStorage)
                    }else{
                        _response.value = progressStatus[3]
                    }
                }
            }
        )
    }

    fun firebaseSignUp(
        firstname: String,
        lastname: String,
        username: String,
        password: String,
        firebaseAuth: FirebaseAuth
    ) {
        //Firebase
        //https://firebase.google.com/docs/auth/android/password-auth
        firebaseAuth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Funsksjon fra ApiService for å registrere brukeren i databasen
                    register(
                        firstname,
                        lastname,
                        username,
                        password
                    )
                } else {
                    Log.d(TAG, "firebaseSignIn:feilet " + task.exception)
                    _response.value = progressStatus[5]
                }
            }
    }

    private fun register(firstname: String, lastname: String, username: String, password1: String) {
        Log.d(TAG, "registrer")
        _response.value = progressStatus[0]
        //Oppretter et nytt bruker-objekt
        val newUser = NewUser(
            firstname,
            "",
            lastname,
            "user",
            username,
            password1,
            true,
            "0.0.0",
            "",
            false
        )
        //registerUser fra ApiService
        LaneLittApi.retrofitService.registerUser(newUser).enqueue(
            object : Callback<Code> {
                override fun onResponse(call: Call<Code>, response: Response<Code>) {
                    Log.d(TAG, "yessss "+ (response.body()?.code ))
                    _response.value = progressStatus[1]
                }
                override fun onFailure(call: Call<Code>, t: Throwable) {
                    Log.d(TAG, "nooooooooo")
                    if(t.message == progressStatus[4]) {
                        Log.d(TAG, "login: onFailure ${t.message}")
                        register(firstname, lastname, username, password1)
                    }else{
                        _response.value = progressStatus[3]
                    }
                }
            }
        )
    }
}

