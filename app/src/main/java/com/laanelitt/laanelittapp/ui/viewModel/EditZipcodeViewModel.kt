package com.laanelitt.laanelittapp.ui.viewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.laanelitt.laanelittapp.data.api.LaneLittApi
import com.laanelitt.laanelittapp.data.model.Code
import com.laanelitt.laanelittapp.data.model.LocalStorage
import com.laanelitt.laanelittapp.data.model.User
import com.laanelitt.laanelittapp.utils.progressStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditZipcodeViewModel (user: User, app: Application) : AndroidViewModel(app) {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response
    //For
    private val _loggedInUser = MutableLiveData<User>()
    val loggedInUser: LiveData<User>
        get() = _loggedInUser

    init {
        _loggedInUser.value = user
    }

    fun updateZipcode(zipcode: String, localStorage: LocalStorage) {
        //Legger til oppdatert postnr til bruker-objektet
        val user = loggedInUser.value
        user?.zipcode = zipcode
        //Starter progressBar
        _response.value = progressStatus[0]
        //ApiService
        user?.id?.let {
            LaneLittApi.retrofitService.editUser(it, user).enqueue(
                object : Callback<Code> {
                    override fun onResponse(call: Call<Code>, response: Response<Code>) {
                        if (response.body()?.city.toString() != "") {
                            Log.d(TAG,"editUser(zipcode): Suksess " +response.body()?.code.toString())
                            //Legger til oppdatert postnr og oppdaterer bruker-objektet som er lagret
                            _response.value = progressStatus[1]
                            user.zipcode = zipcode
                            localStorage.updateUser(user)
                        } else {
                            Log.d(TAG,"editUser(zipcode): Feilet " +response.body()?.code.toString())
                            _response.value = progressStatus[2]
                        }
                    }
                    override fun onFailure(call: Call<Code>, t: Throwable) {
                        println("editUser(zipcode): onFailure$t")
                        _response.value = progressStatus[3]
                    }
                }
            )
        }
    }
}

