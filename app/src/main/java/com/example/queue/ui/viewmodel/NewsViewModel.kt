package com.example.queue.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.data.entities.NewsItem
import com.example.queue.domain.repositories.FirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val _newsData = MutableStateFlow<List<NewsItem>>(emptyList())
    val newsData = _newsData
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading
    private val repository = FirestoreRepository()

    init {
        loadNews()
    }

    private fun loadNews() {
        viewModelScope.launch {
            repository.getNews().collect {
                _isLoading.value = false
                _newsData.value = it
            }
        }
    }

}