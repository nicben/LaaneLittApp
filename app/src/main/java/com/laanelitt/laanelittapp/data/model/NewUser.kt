package com.laanelitt.laanelittapp.objects

data class NewUser (
    var firstname: String?,
    var middlename: String?,
    var lastname: String?,
    var usertype: String?,
    var email: String?,
    var password: String?,
    var terms: Boolean?,
    val birthdate: String?,
    val phone: String?,
    val newsletter:Boolean?
)
