package com.aloysius.voicehealthguide.data.remote.repository.user

import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.ResultState.Success
import com.aloysius.voicehealthguide.data.remote.response.ErrorResponse
import com.aloysius.voicehealthguide.data.remote.retrofit.ApiService
import com.aloysius.voicehealthguide.data.remote.response.NewsResponse
import com.google.gson.Gson
import retrofit2.HttpException

class NewsRepository private constructor(
    private val apiService: ApiService,
) {
    suspend fun getAllNews(): ResultState<NewsResponse> {
        return try {
            val response = apiService.getNews()
            Success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            ResultState.Error(errorResponse.message ?: "An error occurred")
        } catch (e: Exception) {
            ResultState.Error("An error occurred")
        }
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null

        fun getInstance(apiService: ApiService): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService).also { instance = it }
            }
    }
}
