package com.example.queue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.model.repositories.AccountRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _navigateToBaseFragment: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val navigateToBaseFragment = _navigateToBaseFragment
    private val _helpingText = MutableSharedFlow<String?>(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val helpingText = _helpingText
    private val accountRepository = AccountRepository


    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val res = accountRepository.signInAccount(email, password)
            if (res.isSuccess) {
                _navigateToBaseFragment.value = true
            } else {
                val err = when (res.exceptionOrNull()) {
                    is FirebaseAuthInvalidCredentialsException -> "Неверный логин или пароль"
                    else -> res.exceptionOrNull()?.message ?: "Неизвестная ошибка"
                }
                _helpingText.emit(err)
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
            _helpingText.emit(message)
        }
    }
}