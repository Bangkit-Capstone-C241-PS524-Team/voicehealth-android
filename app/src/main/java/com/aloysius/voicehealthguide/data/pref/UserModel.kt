package com.aloysius.dicoding.data.pref

data class UserModel(
    val token: String,
    val isLogin: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)