package com.aloysius.voicehealthguide.data.remote.repository


import com.aloysius.dicoding.data.pref.UserModel
import com.aloysius.dicoding.data.pref.UserPreference
import com.aloysius.voicehealthguide.data.remote.repository.user.LoginRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.NewsRepository
import com.aloysius.voicehealthguide.data.remote.repository.user.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlin.math.log

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val registerRepository: RegisterRepository,
    private val loginRepository: LoginRepository,
    private val newsRepository: NewsRepository
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getRegisterRepository(): RegisterRepository {
        return registerRepository
    }

    fun getLoginRepository(): LoginRepository {
        return loginRepository
    }
    fun getNewsRepository(): NewsRepository {
        return newsRepository
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            registerRepository: RegisterRepository,
            loginRepository: LoginRepository,
            newsRepository: NewsRepository
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, registerRepository, loginRepository, newsRepository).also { instance = it }
            }
    }
}
