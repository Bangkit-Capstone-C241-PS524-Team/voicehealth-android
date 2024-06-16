package com.aloysius.voicehealthguide.view.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aloysius.voicehealthguide.data.ResultState
import com.aloysius.voicehealthguide.data.remote.repository.UserRepository
import com.aloysius.voicehealthguide.data.remote.response.ArticlesItem
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: UserRepository) : ViewModel() {

    private val _listNews = MutableLiveData<List<ArticlesItem>>()
    val listNews: LiveData<List<ArticlesItem>> = _listNews

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getListNews() {
        viewModelScope.launch {
            try {
                when (val response = repository.getNewsRepository().getAllNews()) {
                    is ResultState.Success -> {
                        val filteredArticles =
                            response.data.data.articles.filter { article ->
                                article.urlToImage != null && article.description != null
                            }
                        _listNews.postValue(filteredArticles)
                        Log.d("NewsViewModel", "List updated: ${filteredArticles.size} articles")
                    }

                    is ResultState.Error -> {
                        _errorMessage.postValue(response.error)
                        Log.e("NewsViewModel", "Error: ${response.error}")
                    }

                    is ResultState.Loading -> {
                        // Handle loading state if necessary
                    }
                }
            } catch (e: Exception) {
                Log.e("NewsViewModel", "Exception: ${e.message}", e)
                _errorMessage.postValue("An error occurred")
            }
        }
    }

}
