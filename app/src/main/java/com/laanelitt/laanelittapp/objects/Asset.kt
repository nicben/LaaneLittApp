package com.laanelitt.laanelittapp.objects

import java.io.Serializable

data class Asset(
    val id: Double,
    val assetName: String,
    val description: String,
    val assetImages: List<AssetImage>,
    val users: User
): Serializable




