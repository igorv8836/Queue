package com.example.queue.firebase

import com.example.queue.add_classes.NewsItem
import com.example.queue.listeners.NewsLoadedListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object FirestoreDB{
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    fun getNewsData(listener: NewsLoadedListener){
        firebaseFirestore.collection("news")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                val data: ArrayList<NewsItem> = ArrayList()
                if (it.isSuccessful){
                    val docs = it.result.documents
                    for (i in docs){
                        data.add(NewsItem(
                            i.get("title").toString(),
                            i.get("text").toString(),
                            (i.get("date") as com.google.firebase.Timestamp).toDate()))
                    }
                    listener.newsLoaded(data)
                } else {
                    listener.newsNotLoaded("Возникла ошибка")
                }
            }
    }
}