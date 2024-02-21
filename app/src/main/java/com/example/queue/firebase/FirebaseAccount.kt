package com.example.queue.firebase

import android.util.Log
import com.example.queue.listeners.TaskCompleteListener
import com.google.firebase.auth.FirebaseAuth

object FirebaseAccount {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerAccount(email: String, password: String, nickname: String, listener: TaskCompleteListener){

        if (email.isEmpty()){
            listener.onErrorFinished("Почта не может быть пустой")
            return
        } else if (password.isEmpty()){
            listener.onErrorFinished("Пароль не может быть пустым")
            return
        } else if (nickname.length < 2){
            listener.onErrorFinished("Никнейм должен содержать как минимум 2 знака")
            return
        }

        mAuth.createUserWithEmailAndPassword(email, nickname).addOnCompleteListener {
            if (it.isSuccessful){
                listener.onSuccessFinished()
                Log.i("firebase", "аккаунт зарегистрирован")
            } else {
                if (it.exception is com.google.firebase.auth.FirebaseAuthWeakPasswordException){
                    listener.onErrorFinished("В пароле должно быть не меньше 6 символов!")
                    return@addOnCompleteListener
                }
                listener.onErrorFinished(it.exception.toString())
                Log.i("firebase", it.exception.toString())
            }
        }
    }

    fun signInAccount(email: String, password: String, listener: TaskCompleteListener){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
            if (it.isSuccessful){
                listener.onSuccessFinished()
            } else {
                listener.onErrorFinished(it.exception.toString())
            }
        }
    }

    fun recoverPassword(email: String, listener: TaskCompleteListener){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful){
                listener.onSuccessFinished()
            } else {
                listener.onErrorFinished(it.exception.toString())
            }
        }
    }

    fun changePassword(password: String, listener: TaskCompleteListener){
        mAuth.currentUser?.updatePassword(password)?.addOnCompleteListener {
            if (it.isSuccessful){
                listener.onSuccessFinished()
            } else {
                listener.onErrorFinished(it.exception.toString())
            }
        }
    }

    fun signOut(){
        mAuth.signOut()
    }

}