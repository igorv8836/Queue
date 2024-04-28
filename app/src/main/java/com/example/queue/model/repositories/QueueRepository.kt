package com.example.queue.model.repositories

import com.example.queue.model.firebase.QueueFirestoreDB

object QueueRepository {
    private val firestore = QueueFirestoreDB
    suspend fun createQueue(
        name: String,
        description: String,
        isOpened: Boolean
    ) = firestore.createQueue(name, description, isOpened)

    suspend fun getQueues() = firestore.getQueues()

    fun getErrorFlow() = firestore.errorFlow

    fun getCurrentUser() = firestore.getCurrentUserId()

    suspend fun deleteQueue(id: String) = firestore.deleteQueue(id)

    suspend fun exitFromQueue(queueId: String) = firestore.exitFromQueue(queueId)

    suspend fun changeIsStarted(queueId: String, isStarted: Boolean) =
        firestore.changeIsStarting(queueId, isStarted)

    suspend fun getQueue(id: String) = firestore.getQueue(id)

    suspend fun sendInvitation(queueId: String, nickname: String) =
        firestore.sendInvitation(queueId, nickname)

    suspend fun changePosition(queueId: String, id: String, to: Int) =
        firestore.changePosition(queueId, id, to)

    suspend fun getInvitations() = firestore.getInvitations()

    suspend fun applyInvitation(queueId: String) = firestore.applyInvitation(queueId)

    suspend fun declineInvitation(queueId: String) = firestore.declineInvitation(queueId)

    suspend fun restartQueue(queueId: String) = firestore.cleanCompleteUsers(queueId)

    suspend fun completeActionInQueue(queueId: String, userId: String) =
        firestore.completeActionInQueue(queueId, userId)
}