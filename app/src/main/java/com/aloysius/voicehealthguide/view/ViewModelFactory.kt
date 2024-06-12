package com.aloysius.voicehealthguide.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aloysius.voicehealthguide.data.remote.repository.UserRepository
import com.aloysius.voicehealthguide.di.Injection
import com.aloysius.voicehealthguide.view.login.LoginViewModel
import kotlinx.coroutines.runBlocking

class ViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
//            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
//                MainViewModel(userRepository, storyPagingRepository) as T
//            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
//            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
//                RegisterViewModel(userRepository) as T
//            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: runBlocking {
                    ViewModelFactory(
                        Injection.provideRepository(context),
                    )
                }.also { INSTANCE = it }
            }
        }
    }
}
