package com.example.queue.viewmodels

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.queue.firebase.FirebaseAccount
import com.example.queue.listeners.TaskCompleteListener
import java.lang.Exception

class RegisterViewModel : ViewModel() {
    public val nextFragment: MutableLiveData<Boolean> = MutableLiveData()
    public val errorText: MutableLiveData<String> = MutableLiveData()
    val firebaseAccount = FirebaseAccount
    fun registerAccount(email: String, password: String, nickname: String){
        firebaseAccount.registerAccount(
            email, password, nickname, object : TaskCompleteListener{
                override fun onSuccessFinished() {
                    nextFragment.value = true
                }

                override fun onErrorFinished(error: String) {
                    errorText.value = error
                }

            })
    }
}