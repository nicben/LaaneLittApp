package com.laanelitt.laanelittapp.profile

import androidx.lifecycle.MutableLiveData
import java.io.File

class AddAssetViewModel (){
    private val imageFile: MutableLiveData<File> by lazy {
        MutableLiveData()
    }


}