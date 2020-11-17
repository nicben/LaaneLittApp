package com.laanelitt.laanelittapp.objects

import android.content.Context
import android.content.SharedPreferences


class UserLocalStore(context: Context) {
    var userLocalDatabase: SharedPreferences

    fun storeUserData(user: User) {
        val userLocalDatabaseEditor = userLocalDatabase.edit()
        user?.id?.let { userLocalDatabaseEditor.putInt("id", it) }
        userLocalDatabaseEditor.putString("firstname", user.firstname)
        userLocalDatabaseEditor.putString("middlename", user.middlename)
        userLocalDatabaseEditor.putString("lastname", user.lastname)
        userLocalDatabaseEditor.apply()
    }

    fun setUserLoggedIn(loggedIn: Boolean) {
        val userLocalDatabaseEditor = userLocalDatabase.edit()
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn)
        userLocalDatabaseEditor.apply()
    }

    fun clearUserData() {
        val userLocalDatabaseEditor = userLocalDatabase.edit()
        userLocalDatabaseEditor.clear()
        userLocalDatabaseEditor.apply()
    }

    val getLoggedInUser: User?
        get() {
            if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
                return null
            }
            val id = userLocalDatabase.getInt("id", 0)
            val firstname = userLocalDatabase.getString("firstname", "")
            val middlename = userLocalDatabase.getString("middlename", "")
            val lastname = userLocalDatabase.getString("lastname", "")
            val usertype = userLocalDatabase.getString("usertype", "")
//            val password = userLocalDatabase.getString("password", "")
//            val email = userLocalDatabase.getString("email", "")
            return User(id, firstname, middlename, lastname, usertype)
        }

    companion object {
        const val SP_NAME = "userDetails"
    }



    init {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0)
    }
}