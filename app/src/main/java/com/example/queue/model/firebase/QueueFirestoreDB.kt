package com.example.queue.model.firebase

import android.annotation.SuppressLint
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.queue.App
import com.example.queue.add_classes.Invitation
import com.example.queue.add_classes.Member
import com.example.queue.add_classes.Queue
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject

object QueueFirestoreDB {
    @SuppressLint("StaticFieldLeak")
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val storageRef = Firebase.storage.reference
    private val currUser = FirebaseAuth.getInstance().currentUser
    private val _errorFlow = MutableSharedFlow<Exception>()
    val errorFlow: SharedFlow<Exception> = _errorFlow

    private fun emitError(e: Exception) {
        CoroutineScope(Dispatchers.Default).launch {
            _errorFlow.emit(e)
        }
    }

    fun getCurrentUserId() = currUser?.uid ?: ""

    suspend fun getInvitations() = withContext(Dispatchers.IO) {
        try {
            currUser?.uid?.let { userId ->
                val queueRef =
                    firebaseFirestore.collection("queues").whereArrayContains("invitations", userId)
                val list = queueRef.get().await().documents.mapNotNull { doc ->
                    val ownerId = doc.getString("owner")
                        ?: throw IllegalStateException("Document has no owner")
                    val ownerDoc =
                        firebaseFirestore.collection("users").document(ownerId).get().await()
                    val name =
                        doc.getString("name") ?: throw IllegalStateException("Document has no name")
                    val members = doc.get("members") as? List<*>
                        ?: throw IllegalStateException("Members data is not available")
                    val nickname = ownerDoc.getString("nickname")
                        ?: throw IllegalStateException("Owner has no nickname")

                    Invitation(doc.id, name, members.size, nickname)
                }
                return@withContext Result.success<List<Invitation>>(list)
            } ?: throw IllegalStateException("Current user ID is null")
        } catch (e: Exception) {
            _errorFlow.emit(e)
            return@withContext Result.failure(e)
        }
    }

    suspend fun changePosition(queueId: String, userId: String, newPosition: Int): Result<Unit> =
        withContext(Dispatchers.IO) {
            val queueRef = firebaseFirestore.collection("queues").document(queueId)

            try {
                firebaseFirestore.runTransaction { transaction ->
                    val snapshot = transaction.get(queueRef)
                    val members = snapshot["members"] as? MutableList<String> ?: mutableListOf()

                    val currentIndex = members.indexOf(userId)
                    if (currentIndex == -1) {
                        throw IllegalArgumentException("Пользователь не найден в очереди")
                    }

                    if (newPosition !in 0..members.size) {
                        throw IndexOutOfBoundsException("Неверная новая позиция")
                    }

                    members.removeAt(currentIndex)
                    members.add(newPosition.coerceIn(0, members.size), userId)

                    transaction.update(queueRef, "members", members)
                }.await()
                Result.success(Unit)
            } catch (e: Exception) {
                emitError(e)
                Result.failure(e)
            }
        }

    suspend fun createQueue(
        name: String, description: String, isOpened: Boolean
    ) = withContext(Dispatchers.IO) {
        try {
            if (name.length < 3 || description.length < 3) throw Exception("Имя или описание слишком короткие, нужно больше 3 символов")

            val queue = firebaseFirestore.collection("queues").add(
                mapOf(
                    "name" to name,
                    "description" to description,
                    "isStarted" to isOpened,
                    "owner" to currUser?.uid,
                    "members" to listOf(currUser?.uid)
                )
            ).await()
            val res = addMember(queue.id, currUser?.uid ?: "", true, 0)
            if (res.isFailure) throw res.exceptionOrNull() ?: Exception("Неизвестная ошибка")
            return@withContext Result.success(Unit)
        } catch (e: Exception) {
            emitError(e)
            return@withContext Result.failure(e)
        }
    }

    private suspend fun getPhotoUrl(fileRef: String?) = when (fileRef) {
        null, "" -> ""
        else -> storageRef.child(fileRef).downloadUrl.await().toString()
    }


    private suspend fun addMember(queueId: String, userId: String, isAdmin: Boolean, position: Int) =
        withContext(Dispatchers.IO) {
            try {
                firebaseFirestore.collection("queues").document(queueId).collection("members")
                    .document(userId).set(
                        mapOf(
                            "isAdmin" to isAdmin, "position" to position
                        )
                    ).await()
                return@withContext Result.success(Unit)
            } catch (e: Exception) {
                emitError(e)
                return@withContext Result.failure(e)
            }
        }

    suspend fun completeActionInQueue(queueId: String, userId: String) {
        withContext(Dispatchers.IO) {
            try {
                firebaseFirestore.collection("queues").document(queueId)
                    .update("completeUsers", FieldValue.arrayUnion(userId)).addOnCompleteListener {
                        if (!it.isSuccessful) emitError(
                            it.exception ?: Exception("Неизвестная ошибка")
                        )
                    }
            } catch (e: Exception) {
                emitError(e)
            }
        }
    }

    suspend fun cleanCompleteUsers(queueId: String) {
        withContext(Dispatchers.IO) {
            try {
                firebaseFirestore.collection("queues").document(queueId)
                    .update("completeUsers", FieldValue.delete()).addOnCompleteListener {
                        if (!it.isSuccessful) emitError(
                            it.exception ?: Exception("Неизвестная ошибка")
                        )
                    }
            } catch (e: Exception) {
                emitError(e)
            }
        }
    }

    suspend fun getQueue(id: String) = channelFlow {
        val listener = firebaseFirestore.collection("queues").document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    emitError(error)
                    return@addSnapshotListener
                }
                launch {
                    try {
                        val inactiveUsers =
                            (snapshot?.get("completeUsers") as? List<String>) ?: emptyList()
                        val queue = snapshot?.let {
                            val members =
                                (snapshot.get("members") as List<String>).mapIndexed { _, member ->
                                    val userData =
                                        firebaseFirestore.collection("users").document(member).get()
                                            .await()
                                    Member(
                                        member,
                                        userData["nickname"] as String,
                                        it.getString("owner") == member,
                                        getPhotoUrl((userData["photoPath"] ?: "") as String),
                                        !inactiveUsers.contains(member)
                                    )
                                }.sortedByDescending { it.isActive }
                            val creator = if (members.isEmpty()) Member("", "", false, "", true)
                            else members.first { it.id == snapshot["owner"] }
                            Queue(
                                snapshot.id,
                                snapshot["name"] as String,
                                snapshot["description"] as String,
                                members,
                                snapshot["isStarted"] as Boolean,
                                creator,
                                inactiveUsers
                            )
                        }
                        trySend(queue)
                    } catch (e: Exception) {
                        _errorFlow.tryEmit(e)
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun getQueues(): Flow<Pair<List<Queue>, List<Queue>>> = channelFlow {
        val queuesListener = firebaseFirestore.collection("queues")
            .whereArrayContains("members", currUser?.uid ?: "")
            .addSnapshotListener { snapshot, queueError ->
                if (queueError != null) {
                    _errorFlow.tryEmit(queueError)
                    return@addSnapshotListener
                }

                launch {
                    val queues = snapshot?.documents?.map { queue ->
                        val inactiveUsers =
                            (queue.get("completeUsers") as? List<String>) ?: emptyList()
                        async(Dispatchers.IO) {
                            val members =
                                (queue.get("members") as List<String>).mapIndexed { _, member ->
                                    val userData =
                                        firebaseFirestore.collection("users").document(member).get()
                                            .await()
                                    Member(
                                        member,
                                        userData["nickname"] as String,
                                        queue.getString("owner") == member,
                                        getPhotoUrl((userData["photoPath"] ?: "") as String),
                                        !inactiveUsers.contains(member)
                                    )
                                }
                            val creator = if (members.isEmpty()) Member("", "", false, "", true)
                            else members.first { it.id == queue["owner"] }
                            Queue(
                                queue.id,
                                queue["name"] as String,
                                queue["description"] as String,
                                members,
                                queue["isStarted"] as Boolean,
                                creator,
                                inactiveUsers
                            )
                        }
                    }?.awaitAll()

                    val myQueue = queues?.filter { it.owner.id == currUser?.uid } ?: emptyList()
                    val otherQueues = queues?.filter { it.owner.id != currUser?.uid } ?: emptyList()

                    trySend(Pair(myQueue, otherQueues))
                }
            }

        awaitClose { queuesListener.remove() }
    }

    suspend fun deleteQueue(id: String): Result<Boolean> = withContext(Dispatchers.IO) {
        val doc = firebaseFirestore.collection("queues").document(id)
        doc.collection("members").get().await().documents.forEach { it.reference.delete() }
        val res = doc.delete()
        res.await()
        return@withContext Result.success(res.isSuccessful)
    }

    suspend fun exitFromQueue(queueId: String) = withContext(Dispatchers.IO) {
        val queueRef = firebaseFirestore.collection("queues").document(queueId)

        try {
            firebaseFirestore.runTransaction {
                val queue = it.get(queueRef)
                val members = queue["members"] as List<*>

                if (members.contains(currUser?.uid)) {
                    it.update(queueRef, "members", FieldValue.arrayRemove(currUser?.uid))
                }
            }.await()
        } catch (e: Exception) {
            emitError(e)
            return@withContext Result.success(false)
        }

        queueRef.collection("members").document(currUser?.uid ?: "").delete().await()
        return@withContext Result.success(true)
    }

    suspend fun changeIsStarting(queueId: String, isStarted: Boolean) =
        withContext(Dispatchers.IO) {
            firebaseFirestore.collection("queues").document(queueId).update("isStarted", isStarted)
                .await()
            return@withContext Result.success(true)
        }

    suspend fun sendInvitation(queueId: String, nickname: String): Result<Unit> {
        return try {
            val userId =
                firebaseFirestore.collection("users").whereEqualTo("nickname", nickname).get()
                    .await().documents[0].id
            firebaseFirestore.collection("queues").document(queueId)
            val queueRef = firebaseFirestore.collection("queues").document(queueId)
            val updateData = mapOf("invitations" to FieldValue.arrayUnion(userId))
            queueRef.set(updateData, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: IndexOutOfBoundsException) {
            val err = IllegalArgumentException("Такой пользователь не найден")
            emitError(err)
            Result.failure(err)
        } catch (e: Exception) {
            emitError(e)
            Result.failure(e)
        }

    }

    suspend fun applyInvitation(queueId: String) = withContext(Dispatchers.IO) {
        try {
            val userId = currUser?.uid ?: throw NullPointerException("Пользователь не авторизован")
            val queueRef = firebaseFirestore.collection("queues").document(queueId)

            firebaseFirestore.runTransaction { transaction ->
                transaction.update(queueRef, "invitations", FieldValue.arrayRemove(userId))
                transaction.update(queueRef, "members", FieldValue.arrayUnion(userId))
            }.await()
            return@withContext Result.success(Unit)
        } catch (e: Exception) {
            _errorFlow.emit(e)
            return@withContext Result.failure(e)
        }

    }

    suspend fun declineInvitation(queueId: String) = withContext(Dispatchers.IO) {
        try {
            val userId = currUser?.uid ?: throw NullPointerException("Пользователь не авторизован")
            val queueRef = firebaseFirestore.collection("queues").document(queueId)

            queueRef.update("invitations", FieldValue.arrayRemove(userId)).await()
            return@withContext Result.success(Unit)
        } catch (e: Exception) {
            _errorFlow.emit(e)
            return@withContext Result.failure(e)
        }

    }

    suspend fun setNotificationToken(token: String) = withContext(Dispatchers.IO) {
        currUser?.uid?.let {
            firebaseFirestore.collection("users").document(it).update("notificationToken", token)
                .await()
        }
    }

    suspend fun setNotificationToken(){
        try{
            FirebaseMessaging.getInstance().token.await().let {
                setNotificationToken(it)
            }
        } catch (e: Exception){
            emitError(e)
        }
    }

    private suspend fun getNotificationToken(userId: String) =
        firebaseFirestore.collection("users").document(userId).get().await()
            .getString("notificationToken")

    suspend fun sendNotification(
        userId: String,
        queueName: String
    ): Request<JSONObject> =
        withContext(Dispatchers.IO) {
            val token = getNotificationToken(userId)
            val url = "https://fcm.googleapis.com/fcm/send"
            val requestBody = JSONObject().apply {
                put("to", token)
                put("priority", "high")
                val notification = JSONObject().apply {
                    put("title", "Твоя очередь")
                    put("body", "Ты первый в очереди ${queueName}!")
                }
                put("notification", notification)
            }
            val requestQueue = Volley.newRequestQueue(App.instance)
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.POST, url, requestBody, Response.Listener { _ ->
                }, Response.ErrorListener { _ ->
                }) {
                    override fun getHeaders(): MutableMap<String, String> = hashMapOf(
                        "Content-Type" to "application/json",
                        "Authorization" to "key=AAAAFOcdaKI:APA91bGtlnaAmCJZGhlx2rLXk6bomsv5dNBvthwI1FiX82URfXYSH6uul6x0cO4n25ktrBWhSwTeDXHDHBUE6GBlMTFKlbtdu1BGjAgx7EP5p28wBK8H7kfQ9Uh3ZcpORH3H1zozxUd9"
                    )
                }
            requestQueue.add(jsonObjectRequest)
        }
}