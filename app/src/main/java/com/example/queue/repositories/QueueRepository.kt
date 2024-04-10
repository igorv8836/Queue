package com.example.queue.repositories

import com.example.queue.firebase.QueueFirestoreDB

object QueueRepository {
    private val firestore = QueueFirestoreDB
    suspend fun createQueue(
        name: String,
        description: String,
        isOpened: Boolean,
        isPeriodic: Boolean
    ) = firestore.createQueue(name, description, isOpened, isPeriodic)

    suspend fun getQueues() = firestore.getQueues()

    fun getErrorFlow() = firestore.errorFlow

    fun getCurrentUser() = firestore.getCurrentUserId()

    suspend fun deleteQueue(id: String) = firestore.deleteQueue(id)

    suspend fun exitFromQueue(queueId: String) = firestore.exitFromQueue(queueId)

    suspend fun changeIsStarted(queueId: String, isStarted: Boolean) =
        firestore.changeIsStarting(queueId, isStarted)

    suspend fun getQueue(id: String) = firestore.getQueue(id)
}