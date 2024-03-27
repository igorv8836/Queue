package com.example.queue.repositories

import android.net.Uri
import com.example.queue.firebase.FirestoreDB
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FirestoreRepository {
    private val firestore = FirestoreDB
    suspend fun getNews() = firestore.getNewsData()
    suspend fun getNickname() = firestore.getNickname()
    suspend fun getUserImage() = withContext(Dispatchers.IO){
        firestore.getUserImagePath().getOrNull()?.let {
            val res = firestore.downloadImage(it)
            return@withContext Result.success(res.getOrNull())
        }
        return@withContext Result.failure(Exception("Error"))
    }
    suspend fun setPhoto(imageUri: Uri) = withContext(Dispatchers.IO) {
        try {
            val res = firestore.uploadImage(imageUri)
            res.getOrNull()?.let { firestore.savePhotoPath(it) }
            return@withContext Result.success(Unit)
        } catch (e: Exception){
            return@withContext Result.failure(e)
        }
    }

    suspend fun changeNickname(nickname: String) = firestore.setNickname(nickname)
}