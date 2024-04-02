package com.example.queue.firebase

import android.annotation.SuppressLint
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object QueueFirestoreDB {
    @SuppressLint("StaticFieldLeak")
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val storageRef = Firebase.storage.reference
    private val currUser = FirebaseAuth.getInstance().currentUser
    private val _errorFlow = MutableSharedFlow<Exception>()
    val errorFlow: SharedFlow<Exception> = _errorFlow
    suspend fun createQueue(
        name: String,
        description: String,
        isOpened: Boolean,
        isPeriodic: Boolean
    ) = withContext(Dispatchers.IO){
        try {
            if (name.length < 3 || description.length < 3) {
                val e = Exception("Имя или описание слишком короткие, нужно больше 3 символов")
                _errorFlow.emit(e)
                return@withContext Result.failure(e)
            }

            firebaseFirestore.collection("queues")
                .add(mapOf(
                    "name" to name,
                    "description" to description,
                    "isOpened" to isOpened,
                    "isPeriodic" to isPeriodic,
                    "owner" to currUser?.uid
                )).await()
            return@withContext Result.success(Unit)
        } catch (e: Exception){
            _errorFlow.emit(e)
            return@withContext Result.failure(e)
        }
    }
}