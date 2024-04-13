package com.example.queue.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.add_classes.Queue
import com.example.queue.repositories.QueueRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QueueViewModel : ViewModel() {
    private val queueRepository = QueueRepository
    private val _queue = MutableStateFlow<Queue?>(null)
    val queue = _queue.asStateFlow()
    private val _userId = MutableStateFlow<String?>(null)
    val userId = _userId.asStateFlow()
    private val _isDeleted = MutableStateFlow<Boolean>(false)
    val isDeleted = _isDeleted.asStateFlow()
    private val _helpingText = MutableStateFlow<String?>(null)
    val helpingText = _helpingText.asStateFlow()

    init {
        checkError()
    }

    fun getQueue(id: String) {
        viewModelScope.launch {
            queueRepository.getQueue(id).collect{
                _queue.value = it
            }
        }

    }

    fun checkError(){
        viewModelScope.launch {
            queueRepository.getErrorFlow().collect {
                _helpingText.value = it.message
            }
        }
    }

    fun getCurrentUser() {
        _userId.value = queueRepository.getCurrentUser()
    }


    fun deleteQueue(){
        viewModelScope.launch{
            val res = queue.value?.id?.let { queueRepository.deleteQueue(it) }
            res?.let {
                if (it.isSuccess)
                    _isDeleted.value = it.getOrNull() ?: false
            }
        }


    }

    fun exitFromQueue(){
        viewModelScope.launch{
            val res = queue.value?.id?.let { queueRepository.exitFromQueue(it) }
            if (res?.isSuccess == true){
                _isDeleted.value = res.getOrNull() ?: false
            }
        }

    }

    fun restartQueue(){

    }

    fun startQueue(){
        viewModelScope.launch {
            queue.value?.let {queue ->
                queue.id?.let { queueRepository.changeIsStarted(it, !queue.isStarted)  }
            }
        }
    }

    fun sendInvitation(nickname: String){
        viewModelScope.launch {
            queue.value?.id?.let {
                val res = queueRepository.sendInvitation(it, nickname)
                if (res.isSuccess)
                    _helpingText.value = "Приглашение отправлено"
            }
        }
    }
}