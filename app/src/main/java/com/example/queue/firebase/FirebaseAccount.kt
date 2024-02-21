package com.example.queue.firebase

import com.google.firebase.auth.FirebaseAuth

class FirebaseAccount {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var instance: FirebaseAccount? = null

    public fun getInstance(): FirebaseAccount?{
        if (instance == null){
            instance = FirebaseAccount()
        }
        return instance
    }

    private constructor(){

    }

    fun registerAccount(email: String, password: String, nickname: String){
        mAuth.createUserWithEmailAndPassword(email, nickname).addOnCompleteListener {
            if (it.isSuccessful){

            } else if (it.isCanceled){

            }
        }
    }

}