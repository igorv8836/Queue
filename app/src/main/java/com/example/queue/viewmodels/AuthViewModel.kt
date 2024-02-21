package com.example.queue.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.queue.firebase.FirebaseAccount
import com.example.queue.listeners.TaskCompleteListener

class AuthViewModel : ViewModel() {
    public val navigateToBaseFragment: MutableLiveData<Boolean> = MutableLiveData()
    public val errorText: MutableLiveData<String> = MutableLiveData()
    private val firebaseAccount = FirebaseAccount
    fun signIn(email: String, password: String){
        firebaseAccount.signInAccount(email, password, object : TaskCompleteListener {
            override fun onSuccessFinished() {
                navigateToBaseFragment.value = true
            }

            override fun onErrorFinished(error: String) {
                errorText.value = error
            }
        })
    }

}