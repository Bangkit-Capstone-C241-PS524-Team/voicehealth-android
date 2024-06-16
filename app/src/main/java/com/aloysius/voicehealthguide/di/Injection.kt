package com.aloysius.voicehealthguide.di

import android.content.Context
import com.aloysius.dicoding.data.pref.UserPreference
import com.aloysius.dicoding.data.pref.dataStore
import com.aloysius.voicehealthguide.data.remote.repository.UserRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.LoginRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.NewsRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.RegisterRepository
import com.aloysius.voicehealthguide.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val registerRepository = RegisterRepository.getInstance(apiService)
        val loginRepository = LoginRepository.getInstance(apiService)
        val newsRepository = NewsRepository.getInstance(apiService)
        return UserRepository.getInstance(userPreference, registerRepository, loginRepository,newsRepository )
    }

    fun provideUserPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context.dataStore)
    }

}

