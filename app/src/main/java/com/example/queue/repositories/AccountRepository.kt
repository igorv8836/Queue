package com.example.queue.repositories

import com.example.queue.firebase.FirebaseAccount
import com.example.queue.firebase.FirestoreDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AccountRepository {
    private val firebase = FirebaseAccount
    private val firestore = FirestoreDB

    suspend fun registerAccount(email: String, password: String, nickname: String) =
        firebase.registerAccount(email, password, nickname)

    suspend fun addNickname(nickname: String) = firestore.setNickname(nickname)

    suspend fun signInAccount(email: String, password: String) =
        firebase.signInAccount(email, password)

    suspend fun recoverPassword(email: String) = firebase.recoverPassword(email)
    fun checkAuth() = firebase.checkAuth()
    suspend fun changePassword(password: String) = firebase.changePassword(password)
    fun signOut() = firebase.signOut()

    fun getEmail() = firebase.getEmail()
}