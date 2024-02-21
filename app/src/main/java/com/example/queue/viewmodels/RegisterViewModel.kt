package com.example.queue.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.queue.firebase.FirebaseAccount
import com.example.queue.listeners.TaskCompleteListener

class RegisterViewModel : ViewModel() {
    public val navigateToBaseFragment: MutableLiveData<Boolean> = MutableLiveData()
    public val errorText: MutableLiveData<String> = MutableLiveData()
    private val firebaseAccount = FirebaseAccount
    fun registerAccount(email: String, password: String, nickname: String){
        firebaseAccount.registerAccount(
            email, password, nickname, object : TaskCompleteListener{
                override fun onSuccessFinished() {
                    navigateToBaseFragment.value = true
                }

                override fun onErrorFinished(error: String) {
                    errorText.value = error
                }

            })
    }
}