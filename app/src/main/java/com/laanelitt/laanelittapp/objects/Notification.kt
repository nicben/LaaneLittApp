package com.laanelitt.laanelittapp.objects

import java.io.Serializable

data class Notification(
    val id: Int?,
    val assets: Asset?,
    val users: AssetOwner?,
    val dateStart: String,
    val dateEnd: String
): Serializable