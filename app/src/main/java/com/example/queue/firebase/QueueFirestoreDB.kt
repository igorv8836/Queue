package com.example.queue.firebase

import android.annotation.SuppressLint
import com.example.queue.add_classes.Member
import com.example.queue.add_classes.Queue
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
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

    suspend fun emitError(Exception: Exception){ _errorFlow.emit(Exception) }
    suspend fun createQueue(
        name: String,
        description: String,
        isOpened: Boolean,
        isPeriodic: Boolean
    ) = withContext(Dispatchers.IO){
        try {
            if (name.length < 3 || description.length < 3)
                throw Exception("Имя или описание слишком короткие, нужно больше 3 символов")

            val queue = firebaseFirestore.collection("queues")
                .add(mapOf(
                    "name" to name,
                    "description" to description,
                    "isOpened" to isOpened,
                    "isPeriodic" to isPeriodic,
                    "owner" to currUser?.uid,
                    "members" to listOf(currUser?.uid)
                )).await()
            val res = addMember(queue.id, currUser?.uid ?: "", true, 0)
            if (res.isFailure) throw res.exceptionOrNull() ?: Exception("Неизвестная ошибка")
            return@withContext Result.success(Unit)
        } catch (e: Exception){
            emitError(e)
            return@withContext Result.failure(e)
        }
    }

    suspend fun getPhotoUrl(fileRef: String?) = when(fileRef){
        null, "" -> ""
        else -> storageRef.child(fileRef).downloadUrl.await().toString()
    }


    suspend fun addMember(queueId: String, userId: String, isAdmin: Boolean, position: Int) = withContext(Dispatchers.IO){
        try {
            firebaseFirestore.collection("queues").document(queueId)
                .collection("members").document(userId)
                .set(mapOf(
                    "isAdmin" to isAdmin,
                    "position" to position
                )).await()
            return@withContext Result.success(Unit)
        } catch (e: Exception){
            emitError(e)
            return@withContext Result.failure(e)
        }
    }

    suspend fun getQueues() = withContext(Dispatchers.IO){
        try {
            val queues = firebaseFirestore.collection("queues")
                .whereArrayContains("members", currUser?.uid ?: "").get().await()
                .documents.map {queue ->
                    val members = queue.reference.collection("members").get().await().documents.map { member ->
                        val userData = firebaseFirestore.collection("users").document(member.id).get().await()
                        Member(
                            member.id,
                            userData["nickname"] as String,
                            member["isAdmin"] as Boolean,
                            getPhotoUrl(userData["photoPath"] as String),
                            (member["position"] as Long).toInt()
                        )
                    }
                    Queue(
                        queue.id,
                        queue["name"] as String,
                        queue["description"] as String,
                        members,
                        queue["isOpened"] as Boolean,
                        queue["isPeriodic"] as Boolean,
                        members.first { it.id == queue["owner"] }
                    )
                }

            val myQueue = queues.filter { it.owner.id == currUser?.uid }
            val otherQueues = queues.filter { it.owner.id != currUser?.uid }


            return@withContext Result.success(Pair<List<Queue>, List<Queue>>(myQueue, otherQueues))
        } catch (e: Exception){
            emitError(e)
            return@withContext Result.failure(e)
        }
    }
}