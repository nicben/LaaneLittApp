package com.laanelitt.laanelittapp.objects

import com.squareup.moshi.Json


data class LoggedInUser(
    @Json(name="0") val user: User
)