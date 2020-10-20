package com.laanelitt.laanelittapp.categorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.objects.Assets
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
    init {

        getCatAssets("11") // Gjør REST-kallet med en gang ViewModel-objektet lages
        println("****************getCatAssetsInit************************")

    }
    private fun getCatAssets(catNr: String) {
        println("****************getCatAssets************************")
        LaneLittApi.retrofitService.getCatAssets(catNr).enqueue(
            object: Callback<List<Assets>> {
                override fun onResponse(call: Call<List<Assets>>,
                                        response: Response<List<Assets>>
                ) {
                    println("YAY******************************************************")
                    _response.value = "Ok: ${response.body()!!.size} Assets er hentet."
                    _assets.value = response.body()
                }
                override fun onFailure(call: Call<List<Assets>>, t: Throwable) {
                    println("NAY******************************************************")
                    _response.value = "Feil: " + t.message
                    _assets.value=ArrayList()
                }
            }
        )
    }
    /*fun displayAsset(asset: Assets){
        _navigateToSelectedAsset.value=asset
    }
    fun displayAssetComplete(){
        _navigateToSelectedAsset.value=null
    }*/

}