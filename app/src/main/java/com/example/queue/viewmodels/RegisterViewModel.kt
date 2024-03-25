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
            val res2 = accRepository.addNickname(nickname)
            if (res.isSuccess && res2.isSuccess) {
                _navigateToBaseFragment.postValue(true)
            } else if (res.isFailure) {
                errorText.postValue(res.exceptionOrNull()?.message)
            } else
                errorText.postValue(res2.exceptionOrNull()?.message)
        }

    }
}