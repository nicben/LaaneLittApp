package com.laanelitt.laanelittapp.objects

data class Asset(
    val id: Double,
    val assetName: String,
    val description: String,
    val AssetImage: List<AssetImage>,
    val user: User
)




