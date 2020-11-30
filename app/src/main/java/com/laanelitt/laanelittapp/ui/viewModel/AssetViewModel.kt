/*
 *  Copyright 2019, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.laanelitt.laanelittapp.ui.viewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.laanelitt.laanelittapp.data.api.LaneLittApi
import com.laanelitt.laanelittapp.data.model.Asset
import com.laanelitt.laanelittapp.data.model.Loan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AssetViewModel(asset: Asset, app: Application) : AndroidViewModel(app) {

    //MutableLiveData for valgt eiendel
    private val _selectedAsset = MutableLiveData<Asset>()

    //LiveData for valgt eiendel til bruk for UI
    val selectedAsset: LiveData<Asset>
        get() = _selectedAsset

    // Initierer _selectedProperty MutableLiveData
    init {
        _selectedAsset.value = asset
    }

    fun sendLoanRequest(userId: Int, assetId: Int, startDate: String, endDate: String) {
        val newLoan = Loan(startDate, endDate)
        //ApiService
        LaneLittApi.retrofitService.sendLoanRequest(userId, assetId, newLoan).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d(TAG,"LaneLittApi: onResponse " + response.body())
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d(TAG,"LaneLittApi: onFailure $t")
                }
            }
        )
    }//end sendLoanRequest


    fun editAsset(userId: Int, assetName: String, assetDescription: String, editAsset: Asset) {
        editAsset.assetName = assetName
        editAsset.description = assetDescription
        //ApiService
        LaneLittApi.retrofitService.editAsset(userId, editAsset.id, editAsset).enqueue(
            object : Callback<Asset> {
                override fun onResponse(call: Call<Asset>, response: Response<Asset>) {
                    Log.d(TAG,"editAsset: onResponse " + response.body()?.toString())
                }
                override fun onFailure(call: Call<Asset>, t: Throwable) {
                    Log.d(TAG,"editAsset: onFailure ${t}")
                }
            }
        )
    }//end editAsset

    fun deleteAsset(assetId: Int) {
        //ApiService
        LaneLittApi.retrofitService.deleteAsset(assetId).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d(TAG,"deleteAsset: onResponse " + response.body().toString())
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d(TAG, "deleteAsset: onFailure ${t}")
                }
            }
        )
    }//end deleteAsset
}

