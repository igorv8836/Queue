package com.example.queue.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.queue.repositories.AccountRepository

class MainViewModel: ViewModel() {
    private val _navigateToBaseFragment: MutableLiveData<Boolean> = MutableLiveData()
    val navigateToBaseFragment: LiveData<Boolean> = _navigateToBaseFragment
    private val accountRepository = AccountRepository

    init {
        checkAuth()
    }

    private fun checkAuth(){
        val res = accountRepository.checkAuth()
        _navigateToBaseFragment.value = res
    }
}