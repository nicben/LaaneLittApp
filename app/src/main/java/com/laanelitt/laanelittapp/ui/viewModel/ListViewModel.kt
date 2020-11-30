package com.laanelitt.laanelittapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laanelitt.laanelittapp.data.api.LaneLittApi
import com.laanelitt.laanelittapp.data.model.Asset
import com.laanelitt.laanelittapp.utils.progressStatus
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
    private val _navigateToSelectedAsset = MutableLiveData<Asset>()
    val navigateToSelectedAsset: LiveData<Asset>
        get() = _navigateToSelectedAsset

    fun getCatAssets(userId: Int, catNr: String) {
        viewModelScope.launch {
            _response.value = progressStatus[0]
            try {
                val listResult = LaneLittApi.retrofitService.getCatAssets(userId, catNr)
                _assets.value = listResult
                _response.value = progressStatus[1]
            } catch (e: Exception) {
                //APIet er av og til tregt, og Retrofit er utolmodig, så vi må kjøre API kallet på nytt
                if(e.message == progressStatus[4]){
                    _response.value = progressStatus[2]
                    getCatAssets(userId, catNr)
                }else{
                    _response.value = progressStatus[3]
                }
            }
        }
    }


    fun getMyAssets(userId:Int) {
        viewModelScope.launch {
            _response.value = progressStatus[0]
            try {
                val listResult = LaneLittApi.retrofitService.getMyAssets(userId)
                _assets.value = listResult
                _response.value = progressStatus[1]
            } catch (e: Exception) {
                //APIet er av og til tregt, og Retrofit er utolmodig, så vi må kjøre API kallet på nytt
                if(e.message == progressStatus[4]){
                    _response.value = progressStatus[2]
                    getMyAssets(userId)
                }else{
                    _response.value = progressStatus[3]
                }
            }
        }
    }

    fun getAssetSearch(userId:Int, search:String) {
        viewModelScope.launch {
            _response.value = progressStatus[0]
            try {
                val listResult = LaneLittApi.retrofitService.getAssetSearch(userId, search)
                _assets.value = listResult
                _response.value = progressStatus[1]
            } catch (e: Exception) {
                //APIet er av og til tregt, og Retrofit er utolmodig, så vi må kjøre API kallet på nytt
                if(e.message == progressStatus[4]){
                    _response.value = progressStatus[2]
                    getAssetSearch(userId, search)
                }else{
                    _response.value = progressStatus[3]
                }
            }
        }
    }

    fun displayAssetDetails(asset: Asset) {
        _navigateToSelectedAsset.value = asset
    }

    fun displayAssetDetailsComplete() {
        _navigateToSelectedAsset.value = null
    }

}