package com.example.queue.model.firebase

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseAccount {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun registerAccount(email: String, password: String): Result<String?> =
        withContext(Dispatchers.IO) {
            if (email.isEmpty() || password.isEmpty()) {
                return@withContext Result.failure(IllegalArgumentException("Все поля должны быть заполнены"))
            } else if (password.length < 6) {
                return@withContext Result.failure(IllegalArgumentException("Пароль должен содержат не менее 6 символов"))
            }

            try {
                val res = mAuth.createUserWithEmailAndPassword(email, password).await()
                Result.success(res.user?.uid)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun signInAccount(email: String, password: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (email.isEmpty() || password.isEmpty()) {
                return@withContext Result.failure(IllegalArgumentException("Логин и пароль должны быть заполнены"))
            }

            return@withContext try {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    it.exception?.let { it1 -> throw it1 }
                }.await()
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

    suspend fun changePassword(lastPassword: String, newPassword: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (newPassword.isEmpty() || lastPassword.isEmpty()) {
                return@withContext Result.failure(IllegalArgumentException("Пароль не может быть пустым"))
            }
            val credential =
                EmailAuthProvider.getCredential(mAuth.currentUser?.email!!, lastPassword)
            try {
                mAuth.currentUser!!.reauthenticate(credential).await()
                mAuth.currentUser!!.updatePassword(newPassword).await()
                return@withContext Result.success(Unit)
            } catch (e: NullPointerException) {
                return@withContext Result.failure(NullPointerException("Произошла критическая ошибка"))
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }

    fun getEmail() = mAuth.currentUser?.email

    fun checkAuth() = mAuth.currentUser != null
    fun signOut() {
        mAuth.signOut()
        val a = mAuth.currentUser
        Log.i("test", a?.email.toString())
        Log.i("test", a?.uid.toString())
    }
}

