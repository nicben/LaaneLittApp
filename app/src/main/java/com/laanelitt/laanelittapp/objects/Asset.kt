package com.laanelitt.laanelittapp.objects

import java.io.Serializable

data class Asset(
    val id: Int,
    var assetName: String,
    var description: String?,
    val assetImages: List<AssetImage>,
    val users: AssetOwner,
    var assetType: AssetType?,
    val assetCondition: Int,
    val public: Boolean
): Serializable




