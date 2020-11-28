package com.laanelitt.laanelittapp.objects

import android.content.Context
import android.content.SharedPreferences


class LocalStorage(context: Context) {
    private var userLocalDatabase: SharedPreferences = context.getSharedPreferences("userDetails", 0)
    private var imageLocalDatabase: SharedPreferences = context.getSharedPreferences("image", 0)

    fun storeUserData(user: User) {
        val userLocalDatabaseEditor = userLocalDatabase.edit()
        user.id?.let { userLocalDatabaseEditor.putInt("id", it) }
        userLocalDatabaseEditor.putString("firstname", user.firstname)
        userLocalDatabaseEditor.putString("lastname", user.lastname)
        userLocalDatabaseEditor.putString("zipcode", user.zipcode)
        userLocalDatabaseEditor.putString("profileImage", user.profileImage)
        userLocalDatabaseEditor.putString("email", user.email)
        user.terms?.let { userLocalDatabaseEditor.putBoolean("terms", it) }
        userLocalDatabaseEditor.apply()
    }

    fun updateUser(user: User) {
        val userLocalDatabaseEditor = userLocalDatabase.edit()
        user.id?.let { userLocalDatabaseEditor.putInt("id", it) }
        userLocalDatabaseEditor.putString("firstname", user.firstname)
        userLocalDatabaseEditor.putString("middlename", user.middlename)
        userLocalDatabaseEditor.putString("lastname", user.lastname)
        userLocalDatabaseEditor.putString("zipcode", user.zipcode)
        userLocalDatabaseEditor.putString("profileImage", user.profileImage)
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

    fun setNewPicture(picture: String?) {
        val imageLocalDatabaseEditor = imageLocalDatabase.edit()
        imageLocalDatabaseEditor.putString("newPicture", picture)
        imageLocalDatabaseEditor.apply()
    }

    val getNewPicture: String?
        get() {
            return imageLocalDatabase.getString("newPicture", "")
        }

    val getLoggedInUser: User?
        get() {
            if (!userLocalDatabase.getBoolean("loggedIn", false)) {
                return null
            }
            val id = userLocalDatabase.getInt("id", 0)
            val firstname = userLocalDatabase.getString("firstname", "")
            val middlename = userLocalDatabase.getString("middlename", "")
            val lastname = userLocalDatabase.getString("lastname", "")
            val usertype = userLocalDatabase.getString("usertype", "")
            val profileImage = userLocalDatabase.getString("profileImage", "")
            val zipcode = userLocalDatabase.getString("zipcode", "")
            val password = userLocalDatabase.getString("password", "")
            val email = userLocalDatabase.getString("email", "")
            val terms = userLocalDatabase.getBoolean("terms", false)
            return User(id, firstname, middlename, lastname, usertype, profileImage, email, password, zipcode, terms)
        }

}