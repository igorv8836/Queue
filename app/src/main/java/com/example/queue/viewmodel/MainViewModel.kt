package com.example.queue.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.queue.model.repositories.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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
    }
}