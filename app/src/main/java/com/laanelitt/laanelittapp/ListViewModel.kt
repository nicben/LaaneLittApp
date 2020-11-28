package com.laanelitt.laanelittapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laanelitt.laanelittapp.objects.Asset
import kotlinx.coroutines.launch

class ListViewModel: ViewModel(){

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

                _response.value = "Success: ${listResult.size}  assets retrieved"
                _assets.value = listResult

            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"

                //APIet er av og til tregt, og Retrofit er utolmodig, så vi må kjøre API kallet på nytt
                if(e.message=="timeout"){
                    getCatAssets(catNr)
                }
            }
        }
    }


    fun getMyAssets(userId:Int) {
        viewModelScope.launch {
            try {

                val listResult = LaneLittApi.retrofitService.getMyAssets(userId)

                _response.value = "Success: ${listResult.size}  assets retrieved"
                _assets.value = listResult

            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"

                //APIet er av og til tregt, og Retrofit er utolmodig, så vi må kjøre API kallet på nytt
                if(e.message=="timeout") {
                    getMyAssets(userId)
                }
            }
        }
    }

    fun getAssetSearch(userId:Int, search:String) {
        viewModelScope.launch {
            try {
                val listResult = LaneLittApi.retrofitService.getAssetSearch(userId, search)

                _response.value = "Success: ${listResult.size}  assets retrieved"
                _assets.value = listResult

            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
                //APIet er av og til tregt, og Retrofit er utolmodig, så vi må kjøre API kallet på nytt
                if(e.message=="timeout") {
                    getAssetSearch(userId, search)
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