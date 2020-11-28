package com.laanelitt.laanelittapp.profile.settings.editzipcode

import android.app.Application
import android.content.ContentValues
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.R
import com.laanelitt.laanelittapp.databinding.FragmentEditZipcodeBinding
import com.laanelitt.laanelittapp.objects.Code
import com.laanelitt.laanelittapp.objects.LocalStorage
import com.laanelitt.laanelittapp.objects.User
import kotlinx.android.synthetic.main.fragment_edit_zipcode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditZipcodeViewModel (user: User, app: Application) : AndroidViewModel(app) {
    private val _loggedInUser = MutableLiveData<User>()

    // The external LiveData for the SelectedProperty
    val loggedInUser: LiveData<User>
        get() = _loggedInUser

    // Initialize the _selectedProperty MutableLiveData
    init {
        _loggedInUser.value = user
    }

    fun updateZipcode(zipcode: String, user: User, localStorage: LocalStorage) {
        //Legger til oppdatert postnr til bruker-objektet
        user.zipcode = zipcode
        //ApiService
        user.id?.let {
            LaneLittApi.retrofitService.editUser(it, user).enqueue(
                object : Callback<Code> {
                    override fun onResponse(call: Call<Code>, response: Response<Code>) {
                        if (response.body()?.city.toString() != "") {
                            println( "editUser(zipcode): Suksess " +response.body()?.code.toString())
                            //Legger til oppdatert postnr og oppdaterer bruker-objektet som er lagret
                            user.zipcode = zipcode
                            localStorage.updateUser(user)
                        } else {
                            println("editUser(zipcode): Feilet " +response.body()?.code.toString())

//                            edit_zipcode.error = "Ugyldig postnummer"
                        }
                    }

                    override fun onFailure(call: Call<Code>, t: Throwable) {
                        println("editUser(zipcode): onFailure$t")
                    }
                }
            )
        }
    }
}

