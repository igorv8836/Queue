package com.example.queue.repositories

import android.net.Uri
import com.example.queue.firebase.FirestoreDB
import com.google.firebase.storage.StorageReference

object FirestoreRepository {
    private val firestore = FirestoreDB
    suspend fun getNews() = firestore.getNewsData()
    suspend fun getNickname() = firestore.getNickname()
    suspend fun getUserImage(ref: StorageReference) = firestore.downloadImage(ref)
    suspend fun setPhoto(imageUri: Uri) = firestore.uploadImage(imageUri)
}