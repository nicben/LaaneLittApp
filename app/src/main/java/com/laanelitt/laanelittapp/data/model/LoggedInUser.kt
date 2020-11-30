package com.laanelitt.laanelittapp.data.model

import com.squareup.moshi.Json


data class LoggedInUser(
    //Ett ekstra objekt som trenges for å hente en bruker fra APIet ved innlogging, den har ingen nytte ellers
    @Json(name="0") val user: User
)