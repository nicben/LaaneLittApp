package com.laanelitt.laanelittapp.objects

data class User (
    var id: Int?,
    var firstname: String?,
    var middlename: String?,
    var lastname: String?,
    var usertype: String?,
    var profileImage: String?,
    var email: String?,
    var password: String?,
    var zipcode: String?,
    var terms: Boolean?
){
    //Trenges av APIet for å oprette ny bruker
    var birthdate: String = "0.0.0"
    var phone: String? = ""
    var newsletter = false
    val nickname: String? = ""
}