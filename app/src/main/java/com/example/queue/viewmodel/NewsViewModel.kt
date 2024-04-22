package com.example.queue.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.add_classes.NewsItem
import com.example.queue.model.repositories.FirestoreRepository
import kotlinx.coroutines.launch

class NewsViewModel: ViewModel() {
    private val _newsData = MutableLiveData<List<NewsItem>>()
    val newsData: LiveData<List<NewsItem>> = _newsData
    private val _helpingText = MutableLiveData<String>()
    val helpingText: LiveData<String> = _helpingText
    private val repository = FirestoreRepository
    fun loadNews() {
        viewModelScope.launch {
            val res = repository.getNews().collect{
                _newsData.value = it
            }
//            if (res.isSuccess){
//                res.getOrNull()?.let { _newsData.postValue(it) }
//            } else {
//                _helpingText.postValue(res.exceptionOrNull()?.message)
//            }
        }
    }

}