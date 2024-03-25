package com.example.queue.firebase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FirebaseAccount {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun registerAccount(email: String, password: String, nickname: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (email.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
                return@withContext Result.failure(IllegalArgumentException("Все поля должны быть заполнены"))
            } else if (password.length < 6) {
                return@withContext Result.failure(IllegalArgumentException("Пароль должен содержат не менее 6 символов"))
            } else if (nickname.length < 3) {
                return@withContext Result.failure(IllegalArgumentException("Никнейм должен содержать не менее 3 символов"))
            }

            try {
                mAuth.createUserWithEmailAndPassword(email, password).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun signInAccount(email: String, password: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (email.isEmpty() || password.isEmpty()) {
                return@withContext Result.failure(IllegalArgumentException("Логин и пароль должны быть заполнены"))
            }

            try {
                mAuth.signInWithEmailAndPassword(email, password).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun recoverPassword(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        if (email.isEmpty()) {
            return@withContext Result.failure(IllegalArgumentException("Почта не может быть пустой"))
        }
        try {
            mAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changePassword(password: String): Result<Unit> = withContext(Dispatchers.IO) {
        if (password.isEmpty()) {
            return@withContext Result.failure(IllegalArgumentException("Пароль не может быть пустым"))
        }

        try {
            mAuth.currentUser?.updatePassword(password)?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getEmail() = mAuth.currentUser?.email

    fun checkAuth() = mAuth.currentUser != null
    fun signOut() {
        mAuth.signOut()
    }
}

