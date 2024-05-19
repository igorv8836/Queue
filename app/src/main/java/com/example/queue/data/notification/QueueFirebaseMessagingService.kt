package com.example.queue.data.notification

import com.example.queue.data.firebase.QueueFirestoreDB
import com.example.queue.domain.repositories.QueueRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class QueueFirebaseMessagingService : FirebaseMessagingService() {
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private val rep = QueueRepository(QueueFirestoreDB())

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.title?.let {
            NotificationUtils.sendNotification(
                applicationContext,
                it,
                remoteMessage.notification?.body ?: ""
            )
        }


    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        serviceScope.launch {
            rep.setNotificationToken(token)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}