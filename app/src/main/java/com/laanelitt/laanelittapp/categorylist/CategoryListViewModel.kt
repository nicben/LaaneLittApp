package com.laanelitt.laanelittapp.categorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laanelitt.laanelittapp.ApiService
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.objects.Assets
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryListViewModel: ViewModel(){

    // Interne MutableLiveData som lagrer responsen fra APIet

    private val _response = MutableLiveData<String>()
    private val _assets = MutableLiveData<List<Assets>>()

    // Public immutable LiveData som kan brukes av UI

    val response: LiveData<String>
        get() = _response
    val assets: LiveData<List<Assets>>
        get() = _assets

    fun getCatAssets(catNr: String) {
        viewModelScope.launch {
            try {
                println("Test1******************************************************")
                val listResult = LaneLittApi.retrofitService.getCatAssets(catNr)
                println("Test2******************************************************")
                _response.value = "Success: ${listResult.size}  assets retrieved"
                _assets.value = listResult
                println(_response.value)
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
                println("GOD DAMN IT******************************************************")
            }
        }

//        println("****************getCatAssets************************")
//        LaneLittApi.retrofitService.getCatAssets(catNr).enqueue(
//            object: Callback<List<Assets>> {
//                override fun onResponse(call: Call<List<Assets>>,
//                                        response: Response<List<Assets>>
//                ) {
//                    println("YAY******************************************************")
//                    _response.value = "Ok: ${response.body()!!.size} Assets er hentet."
//                    _assets.value = response.body()
//                }
//                override fun onFailure(call: Call<List<Assets>>, t: Throwable) {
//                    println("GOD DAMN IT******************************************************")
//                    _response.value = "Feil: " + t.message
//                    _assets.value=ArrayList()
//                }
//            }
//        )
    }

}