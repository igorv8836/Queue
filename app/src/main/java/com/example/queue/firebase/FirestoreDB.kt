package com.example.queue.firebase

import android.annotation.SuppressLint
import com.example.queue.add_classes.NewsItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FirestoreDB {
    @SuppressLint("StaticFieldLeak")
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getNewsData(): Result<List<NewsItem>> = withContext(Dispatchers.IO){
        try {
            val snapshot = firebaseFirestore.collection("news")
                .orderBy("date", Query.Direction.DESCENDING).get().await()

            val data = snapshot.documents.map { doc ->
                NewsItem(
                    doc.getString("title") ?: error("Пустое название"),
                    doc.getString("text") ?: error("Пустое описание"),
                    doc.getTimestamp("date")?.toDate() ?: error("Пустая дата")
                )
            }
            return@withContext Result.success(data)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }
}