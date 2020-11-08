package com.laanelitt.laanelittapp.objects

data class Assets(
    val id: Double,
    val assetName: String,
    val description: String,
    val AssetImages: List<AssetImages>,
    val users: Users
)




