package com.laanelitt.laanelittapp.categorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laanelitt.laanelittapp.ApiService
import com.laanelitt.laanelittapp.LaneLittApi
import kotlinx.coroutines.launch
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

    // LiveData to handle navigation to the selected property
    private val _navigateToSelectedProperty = MutableLiveData<Asset>()

    val navigateToSelectedProperty: LiveData<Asset>
        get() = _navigateToSelectedProperty

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
                println(e.message + "GOD DAMN IT******************************************************")
            }
        }

    }


    /**
     * When the property is clicked, set the [_navigateToSelectedProperty] [MutableLiveData]
     * @param Asset The [Asset] that was clicked on.
     */
    fun displayPropertyDetails(asset: Asset) {
        _navigateToSelectedProperty.value = asset
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedProperty is set to null
     */
    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

}