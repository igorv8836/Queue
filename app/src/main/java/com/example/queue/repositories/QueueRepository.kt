package com.example.queue.repositories

import com.example.queue.firebase.QueueFirestoreDB

object QueueRepository {
    val firestore = QueueFirestoreDB
    suspend fun getQueuesData() = emptyList<String>()
    suspend fun createQueue(
        name: String,
        description: String,
        isOpened: Boolean,
        isPeriodic: Boolean
    ) = firestore.createQueue(name, description, isOpened, isPeriodic)

    fun getErrorFlow() = firestore.errorFlow
}