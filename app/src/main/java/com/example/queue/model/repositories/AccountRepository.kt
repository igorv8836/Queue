package com.example.queue.model.repositories

import com.example.queue.model.firebase.FirebaseAccount
import com.example.queue.model.firebase.FirestoreDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AccountRepository {
    private val firebase = FirebaseAccount
    private val firestore = FirestoreDB

    suspend fun registerAccount(email: String, password: String, nickname: String): Result<Unit> =
        withContext(Dispatchers.Default) {
            val nicknameRes = firestore.setNickname(nickname)
            if (nicknameRes.isSuccess) {
                val res = firebase.registerAccount(email, password)
                if (res.isFailure){
                    return@withContext Result.failure(res.exceptionOrNull() ?: Exception("Error"))
                }
            } else
                return@withContext Result.failure(nicknameRes.exceptionOrNull() ?: Exception("Error"))
            return@withContext Result.success(Unit)
        }


    suspend fun addNickname(nickname: String) = firestore.setNickname(nickname)

    suspend fun signInAccount(email: String, password: String) =
        firebase.signInAccount(email, password)

    suspend fun recoverPassword(email: String) = firebase.recoverPassword(email)
    fun checkAuth() = firebase.checkAuth()
    suspend fun changePassword(password: String) = firebase.changePassword(password)
    fun signOut() = firebase.signOut()

    fun getEmail() = firebase.getEmail()
}