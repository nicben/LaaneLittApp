package com.laanelitt.laanelittapp.objects


import android.graphics.drawable.Drawable
import java.io.File

data class NewAsset(
    val userId: Int,
    val assetName: String,
    val description: String,
    val typeId: Int
){
    val condition=0
    val public=true
}
