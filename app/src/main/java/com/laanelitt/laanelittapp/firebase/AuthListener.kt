package com.laanelitt.laanelittapp.firebase

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}