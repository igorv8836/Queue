package com.example.queue.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.repositories.AccountRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _navigateToBaseFragment: MutableLiveData<Boolean> = MutableLiveData()
    val navigateToBaseFragment: LiveData<Boolean> = _navigateToBaseFragment
    val errorText: MutableLiveData<String> = MutableLiveData()

    private val accRepository = AccountRepository

    fun registerAccount(email: String, password: String, nickname: String) {
        viewModelScope.launch {
            val res = accRepository.registerAccount(email, password, nickname)
            if (res.isSuccess) {
                _navigateToBaseFragment.postValue(true)
            } else {
                errorText.postValue(res.exceptionOrNull()?.message)
            }
        }

    }
}