package com.laanelitt.laanelittapp.objects

data class AssetOwner(
    var id: Int?,
    var firstName: String?,
    var middleName: String?,
    var lastName: String?,
    var profileImage: String?,
    var password: String?,
    var email: String?
){
    //Trenges av APIet for å oprette ny bruker
    var firstname: String? = ""
    var middlename: String? = ""
    var lastname: String = ""
    var birthdate: String = "0.0.0"
    var phone: String? = ""
    var newsletter = false
    var terms = false
    val usertype: String = ""
    val nickname: String? = ""
}