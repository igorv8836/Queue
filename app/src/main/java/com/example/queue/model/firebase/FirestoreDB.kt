@file:Suppress("DEPRECATION")

package com.example.queue.model.firebase

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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Date
import java.util.UUID

class FirestoreDB {
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val storageRef = Firebase.storage.reference
    private val currUser = FirebaseAuth.getInstance().currentUser
    private val errorChannel = MutableSharedFlow<Exception>()

    fun getNews(): Flow<List<NewsItem>> = callbackFlow {
        val collectionRef = firebaseFirestore.collection("news")
            .orderBy("date", Query.Direction.DESCENDING)

        val listener = collectionRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                errorChannel.tryEmit(e)
                close(e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val data = snapshot.documents.mapNotNull { doc ->
                    try {
                        NewsItem(
                            title = doc.getString("title") ?: "",
                            text = doc.getString("text")?.replace("\\n", "\n") ?: "",
                            date = doc.getTimestamp("date")?.toDate() ?: Date(0)
                        )
                    } catch (e: Exception) {
                        errorChannel.tryEmit(e)
                        null
                    }
                }
                trySend(data)
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun setNickname(nickname: String, uid: String? = null): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (nickname.length <= 3)
                return@withContext Result.failure(IllegalArgumentException("Длина должна быть больше 3 символов"))
            if (currUser == null && uid == null)
                return@withContext Result.failure(NullPointerException("Текущий пользователь не определен"))
            val usersCollection = firebaseFirestore.collection("users")

            if (isUniqueNickname(nickname).isSuccess && !isUniqueNickname(nickname).getOrNull()!!) {
                return@withContext Result.failure(Exception("Никнейм уже занят"))
            }

            val user = hashMapOf("nickname" to nickname)
            usersCollection.document(uid ?: currUser?.uid ?: "").set(user, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
             Result.failure(e)
        }
    }

    suspend fun isUniqueNickname(nickname: String): Result<Boolean> = withContext(Dispatchers.IO){
        try {
            if (nickname.length <= 3)
                return@withContext Result.failure(IllegalArgumentException("Длина должна быть больше 3 символов"))
            val usersCollection = firebaseFirestore.collection("users")
            val querySnapshot = usersCollection.whereEqualTo("nickname", nickname).get().await()
            return@withContext Result.success(querySnapshot.isEmpty)
        } catch (e: Exception){
            return@withContext Result.failure(e)
        }
    }

    suspend fun getNickname(): Flow<String> = channelFlow{
        try {
            if (currUser == null)
                errorChannel.tryEmit(NullPointerException("Пользователь не авторизован"))
            else {
                val res = firebaseFirestore.collection("users")
                    .document(currUser.uid).addSnapshotListener{ nickname, error ->
                        if (error != null) {
                            errorChannel.tryEmit(error)
                            return@addSnapshotListener
                        }
                        trySend(nickname?.get("nickname").toString())
                    }
                awaitClose { res.remove() }
            }
        } catch (e: Exception){
            errorChannel.tryEmit(e)
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