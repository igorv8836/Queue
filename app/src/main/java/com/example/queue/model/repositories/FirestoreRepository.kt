package com.example.queue.model.repositories

import android.net.Uri
import com.example.queue.model.firebase.FirestoreDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirestoreRepository {
    private val firestore = FirestoreDB()
    fun getNews() = firestore.getNews()
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