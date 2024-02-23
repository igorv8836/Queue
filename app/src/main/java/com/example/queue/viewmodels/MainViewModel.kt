package com.example.queue.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.queue.firebase.FirebaseAccount

class MainViewModel: ViewModel() {
    private val firebaseAccount = FirebaseAccount
    public val missAuth = MutableLiveData<Boolean>()

    fun checkAuth(){
        if (firebaseAccount.checkAuth())
            missAuth.value = true
    }
}