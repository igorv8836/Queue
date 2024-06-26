package com.example.queue.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.data.firebase.QueueFirestoreDB
import com.example.queue.domain.repositories.AccountRepository
import com.example.queue.domain.repositories.QueueRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _navigateToBaseFragment: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val navigateToBaseFragment = _navigateToBaseFragment.asStateFlow()
    private val accountRepository = AccountRepository()
    private val queueRepository = QueueRepository(QueueFirestoreDB())

    init {
        checkAuth()
        viewModelScope.launch {
            queueRepository.getAndSetNotificationToken()
        }
    }

    private fun checkAuth(){
        val res = accountRepository.checkAuth()
        _navigateToBaseFragment.value = res

        if (res)
            viewModelScope.launch {
                QueueRepository(QueueFirestoreDB()).setNotificationToken()
            }

    }
}