package com.laanelitt.laanelittapp.objects

data class User(
    var id: Int?,
    var firstName: String?,
    var lastName: String?,
    var profileImage: String?
){
    //Trenges av APIet for å oprette ny bruker

    var firstname: String = ""
    var middlename: String? = ""
    var lastname: String? = ""
    var birthdate: String = "0.0.0"
    var email: String? = ""
    var phone: String? = ""
    var newsletter = false
    var terms = false
    var password: String? = ""
}