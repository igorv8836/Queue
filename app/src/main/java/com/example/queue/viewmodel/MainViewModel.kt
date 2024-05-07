package com.example.queue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.model.repositories.AccountRepository
import com.example.queue.model.repositories.QueueRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _navigateToBaseFragment: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val navigateToBaseFragment = _navigateToBaseFragment.asStateFlow()
    private val accountRepository = AccountRepository

    init {
        checkAuth()
    }

    private fun checkAuth(){
        val res = accountRepository.checkAuth()
        _navigateToBaseFragment.value = res

        if (res)
            viewModelScope.launch {
                QueueRepository.setNotificationToken()
            }

    }
}