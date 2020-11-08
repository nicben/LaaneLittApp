package com.laanelitt.laanelittapp.categorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.objects.Asset
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryListViewModel: ViewModel(){

    // Interne MutableLiveData som lagrer responsen fra APIet

    private val _response = MutableLiveData<String>()
    private val _assets = MutableLiveData<List<Asset>>()

    // Public immutable LiveData som kan brukes av UI

    val response: LiveData<String>
        get() = _response
    val assets: LiveData<List<Asset>>
        get() = _assets

    fun getCatAssets(catNr: String) {
        println("****************getCatAssets************************")
        LaneLittApi.retrofitService.getCatAssets(catNr).enqueue(
            object: Callback<List<Asset>> {
                override fun onResponse(call: Call<List<Asset>>,
                                        response: Response<List<Asset>>
                ) {
                    println("YAY******************************************************")
                    _response.value = "Ok: ${response.body()!!.size} Assets er hentet."
                    _assets.value = response.body()
                }
                override fun onFailure(call: Call<List<Asset>>, t: Throwable) {
                    println("GOD DAMN IT******************************************************")
                    _response.value = "Feil: " + t.message
                    _assets.value=ArrayList()
                }
            }
        )
    }

}