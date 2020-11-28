package com.laanelitt.laanelittapp.profile.settings.editname

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.objects.Code
import com.laanelitt.laanelittapp.objects.LocalStorage
import com.laanelitt.laanelittapp.objects.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditNameViewModel(user: User, app: Application) : AndroidViewModel(app) {
    val status = arrayOf("Success", "Failure")
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    private val _loggedInUser = MutableLiveData<User>()

    // The external LiveData for the SelectedProperty
    val loggedInUser: LiveData<User>
        get() = _loggedInUser

    // Initialize the _selectedProperty MutableLiveData
    init {
        _loggedInUser.value = user
    }

    fun updateName(userId: Int, firstname: String, lastname: String, user: User, localStorage: LocalStorage) {
        //Legger til de oppdaterte navnene til bruker-objektet som skal sendes med editUser
        user.firstname = firstname
        user.lastname = lastname

        //ApiService
        LaneLittApi.retrofitService.editUser(userId, user).enqueue(
            object : Callback<Code> {
                override fun onResponse(call: Call<Code>, response: Response<Code>) {
                    if(response.body()?.code.toString()=="200"){
                        println( "editUser(name): Suksess " +response.body()?.code.toString())
                        _response.value = status[0]
                        //Legger til de oppdaterte navnene og oppdaterer bruker-objektet som er lagret
                        localStorage.updateUser(user)

                    } else {
                        println( "editUser(name): Feilet " +response.body()?.code.toString())
                        _response.value = status[1]
                    }
                }

                override fun onFailure(call: Call<Code>, t: Throwable) {
                    println("editUser(name): onFailure$t")
                    _response.value = status[1]
                }
            }
        )
    }
}