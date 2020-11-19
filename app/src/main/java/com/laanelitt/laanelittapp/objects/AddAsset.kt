package com.laanelitt.laanelittapp.objects

import java.util.concurrent.locks.Condition

data class AddAsset(
    val userId: String,
    val assetName: String,
    val description: String,
    val typeId: Int
){
    val condition=0
    val mainImage=1
    val public=true
}