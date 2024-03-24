package com.example.queue.repositories

import com.example.queue.firebase.FirestoreDB

object FirestoreRepository {
    private val firestore = FirestoreDB
    suspend fun getNews() = firestore.getNewsData()
}