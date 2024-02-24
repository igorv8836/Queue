package com.example.queue.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.queue.add_classes.NewsItem
import com.example.queue.firebase.FirestoreDB
import com.example.queue.listeners.NewsLoadedListener

class NewsViewModel: ViewModel() {
    val newsData = MutableLiveData<ArrayList<NewsItem>>()
    val errorText = MutableLiveData<String>()
    val firestoreDB: FirestoreDB = FirestoreDB
    fun loadNews() {
        firestoreDB.getNewsData(object: NewsLoadedListener{
            override fun newsLoaded(data: ArrayList<NewsItem>) {
                newsData.value = data
            }

            override fun newsNotLoaded(error: String) {
                errorText.value = error
            }
        })
    }

}