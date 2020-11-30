package com.laanelitt.laanelittapp.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.laanelitt.laanelittapp.data.api.LaneLittApi
import com.laanelitt.laanelittapp.data.model.Code
import com.laanelitt.laanelittapp.data.model.LocalStorage
import com.laanelitt.laanelittapp.data.model.User
import com.laanelitt.laanelittapp.utils.progressStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditNameViewModel(user: User, app: Application) : AndroidViewModel(app) {
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

        _response.value = progressStatus[0]

        //ApiService
        LaneLittApi.retrofitService.editUser(userId, user).enqueue(
            object : Callback<Code> {
                override fun onResponse(call: Call<Code>, response: Response<Code>) {
                    if(response.body()?.code.toString()=="200"){
                        println( "editUser(name): Suksess " +response.body()?.code.toString())
                        _response.value = progressStatus[1]
                        //Legger til de oppdaterte navnene og oppdaterer bruker-objektet som er lagret
                        localStorage.updateUser(user)

                    } else {
                        println( "editUser(name): Feilet " +response.body()?.code.toString())
                        _response.value = progressStatus[2]
                    }
                }

                override fun onFailure(call: Call<Code>, t: Throwable) {
                    println("editUser(name): onFailure$t")
                    _response.value = progressStatus[3]
                }
            }
        )
    }
}