package com.example.queue.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.model.repositories.AccountRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _navigateToBaseFragment: MutableLiveData<Boolean> = MutableLiveData()
    val navigateToBaseFragment: LiveData<Boolean> = _navigateToBaseFragment
    private val _helpingText = MutableLiveData<String>()
    val helpingText: LiveData<String> = _helpingText
    private val accountRepository = AccountRepository


    init {
//        accountRepository.signOut()
    }


    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val res = accountRepository.signInAccount(email, password)
            if (res.isSuccess) {
                _navigateToBaseFragment.postValue(true)
            } else {
                val err = when (res.exceptionOrNull()) {
                    is FirebaseAuthInvalidCredentialsException -> "Неверный логин или пароль"
                    else -> res.exceptionOrNull()?.message ?: "Неизвестная ошибка"
                }
                _helpingText.postValue(err)
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            val res = accountRepository.recoverPassword(email)
            val message = if (res.isSuccess) {
                "На почту отправлено письмо для восстановления"
            } else {
                "Критическая ошибка " + res.exceptionOrNull()?.message
            }
            _helpingText.postValue(message)
        }
    }
}