package com.laanelitt.laanelittapp.objects

import com.squareup.moshi.Json


data class LogginUser(
    @Json(name="0") val user: Users
)