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

package com.laanelitt.laanelittapp.asset

import android.app.Application
import androidx.lifecycle.*
import com.laanelitt.laanelittapp.LaneLittApi
import com.laanelitt.laanelittapp.objects.Asset
import com.laanelitt.laanelittapp.objects.Loan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AssetViewModel(asset: Asset, app: Application) : AndroidViewModel(app) {

    // The internal MutableLiveData for the selected property
    private val _selectedProperty = MutableLiveData<Asset>()

    // The external LiveData for the SelectedProperty
    val selectedProperty: LiveData<Asset>
        get() = _selectedProperty

    // Initialize the _selectedProperty MutableLiveData
    init {
        _selectedProperty.value = asset
    }

    fun sendLoanRequest(userId: Int, assetId: Int, startDate: String, endDate: String) {
        val newLoan = Loan(startDate, endDate)
        //ApiService
        LaneLittApi.retrofitService.sendLoanRequest(userId, assetId, newLoan).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    println("LaneLittApi: onResponse " + response.body())
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    println("LaneLittApi: onFailure $t")
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
                    println("editAsset: onResponse " + response.body()?.toString())
                }
                override fun onFailure(call: Call<Asset>, t: Throwable) {
                    println("editAsset: onFailure $t")
                }
            }
        )
    }//end editAsset

    fun deleteAsset(assetId: Int) {
        //ApiService
        LaneLittApi.retrofitService.deleteAsset(assetId).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    println("deleteAsset: onResponse " + response.body().toString())
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    println("deleteAsset: onFailure $t")
                }
            }
        )
    }//end deleteAsset
}

