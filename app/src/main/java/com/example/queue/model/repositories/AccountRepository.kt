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
            val uniqCheck = firestore.isUniqueNickname(nickname)
            if (uniqCheck.isSuccess && !uniqCheck.getOrNull()!!) {
                return@withContext Result.failure(Exception("Такой никнейм уже занят или он слишком короткий"))
            }
            val res = firebase.registerAccount(email, password)
            if (res.isSuccess) {
                val nicknameRes = firestore.setNickname(nickname, res.getOrNull())
                if (nicknameRes.isFailure){
                    return@withContext Result.failure(res.exceptionOrNull() ?: Exception("Nickname Error"))
                }
            } else
                return@withContext Result.failure(res.exceptionOrNull() ?: Exception("Error"))
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