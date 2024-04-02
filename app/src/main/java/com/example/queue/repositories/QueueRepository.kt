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
}