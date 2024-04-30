package com.example.queue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.add_classes.NewsItem
import com.example.queue.model.repositories.FirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val _newsData = MutableStateFlow<List<NewsItem>>(emptyList())
    val newsData = _newsData
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading
    private val repository = FirestoreRepository

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