package com.laanelitt.laanelittapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laanelitt.laanelittapp.objects.Asset
import kotlinx.coroutines.launch
import com.laanelitt.laanelittapp.Resource

class ListViewModel: ViewModel(){
    val success= arrayOf("Success", "Failure", "Critical failure")
    // Interne MutableLiveData som lagrer responsen fra APIet
    private val _response = MutableLiveData<String>()

    private val _assets = MutableLiveData<List<Asset>>()

    // Public immutable LiveData som kan brukes av UI
    val response: LiveData<String>
        get() = _response

    val assets: LiveData<List<Asset>>
        get() = _assets

    // LiveData to handle navigation to the selected property
    private val _navigateToSelectedProperty = MutableLiveData<Asset>()
    val navigateToSelectedProperty: LiveData<Asset>
        get() = _navigateToSelectedProperty

    fun getCatAssets(catNr: String) {
        viewModelScope.launch {
            try {
                val listResult = LaneLittApi.retrofitService.getCatAssets(catNr)
                _response.value = "Success"
                _assets.value = listResult

            } catch (e: Exception) {
                //APIet er av og til tregt, og Retrofit er utolmodig, så vi må kjøre API kallet på nytt
                if(e.message=="timeout"){
                    _response.value = success[1]
                    getCatAssets(catNr)
                }else{
                    _response.value = success[2]
                }
            }
        }
    }


    fun getMyAssets(userId:Int) {
        viewModelScope.launch {
            try {

                val listResult = LaneLittApi.retrofitService.getMyAssets(userId)

                _response.value = "Success"
                _assets.value = listResult

            } catch (e: Exception) {

                //APIet er av og til tregt, og Retrofit er utolmodig, så vi må kjøre API kallet på nytt
                if(e.message=="timeout") {
                    _response.value = success[1]
                    getMyAssets(userId)
                }else{
                    _response.value = success[2]
                }
            }
        }
    }

    fun getAssetSearch(userId:Int, search:String) {
        viewModelScope.launch {
            try {
                val listResult = LaneLittApi.retrofitService.getAssetSearch(userId, search)

                _response.value = "Success"
                _assets.value = listResult

            } catch (e: Exception) {
                //APIet er av og til tregt, og Retrofit er utolmodig, så vi må kjøre API kallet på nytt
                if(e.message=="timeout") {
                    _response.value = success[1]
                    getAssetSearch(userId, search)
                }else{
                    _response.value = success[2]
                }
            }
        }
    }

    fun displayPropertyDetails(asset: Asset) {
        _navigateToSelectedProperty.value = asset
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

}