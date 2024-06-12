package com.aloysius.voicehealthguide.di

object TokenManager {
    @Volatile
    private var token: String? = null

    fun getToken(): String? = token

    fun setToken(newToken: String?) {
        token = newToken
    }
}

