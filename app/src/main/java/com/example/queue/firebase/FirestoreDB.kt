package com.example.queue.firebase

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.OpenableColumns
import com.example.queue.App
import com.example.queue.add_classes.NewsItem
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.util.UUID

object FirestoreDB {
    @SuppressLint("StaticFieldLeak")
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val storageRef = Firebase.storage.reference
    private val currUser = FirebaseAuth.getInstance().currentUser

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

    suspend fun setNickname(nickname: String): Result<Unit> = withContext(Dispatchers.IO){
        try {
            if (nickname.length <= 3)
                return@withContext Result.failure(IllegalArgumentException("Длина должна быть больше 3 символов"))
            if (currUser == null)
                return@withContext Result.failure(NullPointerException())
            val user = hashMapOf("nickname" to nickname)
            val a = firebaseFirestore.collection("users")
                .document(currUser.uid).set(user as Map<String, Any>, SetOptions.merge()).await()
            return@withContext Result.success(Unit)
        } catch (e: Exception){
            return@withContext Result.failure(e)
        }
    }

    suspend fun getNickname(): Result<String> = withContext(Dispatchers.IO){
        try {
            if (currUser == null)
                return@withContext Result.failure(NullPointerException())
            val res = firebaseFirestore.collection("users").document(currUser.uid).get().await()
            return@withContext Result.success(res.get("nickname").toString())
        } catch (e: Exception){
            return@withContext Result.failure(e)
        }
    }

    suspend fun uploadImage(imageUri: Uri): Result<StorageReference> = withContext(Dispatchers.IO) {
        try {
            val cursor = App.instance.contentResolver.query(imageUri, null, null, null, null)
            val size = cursor?.use {
                val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                it.moveToFirst()
                it.getLong(sizeIndex)
            }
            if ((size ?: 0) > 1_048_576)
                return@withContext Result.failure(Exception("Файл не может весить более 1 мб"))
            val imageName = UUID.randomUUID().toString()
            val imageRef = storageRef.child("user_images/$imageName")
            imageRef.putFile(imageUri).await()
            return@withContext Result.success(imageRef)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun downloadImage(fileRef: StorageReference): Result<File> = withContext(Dispatchers.IO) {
        try {
            val byteArray = fileRef.getBytes(1_048_576).await()
            val tempFile = createTempFile("temp", null, App.instance.cacheDir)
            tempFile.outputStream().use { outputStream ->
                outputStream.write(byteArray)
            }
            return@withContext Result.success(tempFile)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }


    suspend fun savePhotoPath(fileRef: StorageReference): Result<Unit> = withContext(Dispatchers.IO){
        try {
            val user = hashMapOf("photoPath" to fileRef.path)
            firebaseFirestore.collection("users")
                .document(currUser!!.uid).set(user, SetOptions.merge()).await()
            return@withContext Result.success(Unit)
        } catch (e: Exception){
            return@withContext Result.failure(e)
        }
    }

    suspend fun getUserImagePath(): Result<StorageReference> = withContext(Dispatchers.IO){
        try {
            if (currUser == null)
                return@withContext Result.failure(NullPointerException())
            val res = firebaseFirestore.collection("users").document(currUser.uid).get().await()
            return@withContext Result.success(storageRef.child(res.get("photoPath").toString()))
        } catch (e: Exception){
            return@withContext Result.failure(e)
        }
    }
}