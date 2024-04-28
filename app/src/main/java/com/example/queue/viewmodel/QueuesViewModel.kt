package com.example.queue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.add_classes.Queue
import com.example.queue.model.repositories.QueueRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class QueuesViewModel: ViewModel() {
    private val repository = QueueRepository
    private val _errorText = MutableSharedFlow<String>()
    val errorText: SharedFlow<String> = _errorText.asSharedFlow()
    private val _queues = MutableStateFlow<List<Queue>>(emptyList())
    open val queues = _queues.asStateFlow()
    private val _myQueues = MutableStateFlow<List<Queue>>(emptyList())
    open val myQueues = _myQueues.asStateFlow()

    val creationComplete = MutableStateFlow(false)
    private val _showLoading = MutableStateFlow(false)
    val showLoading = _showLoading.asStateFlow()
    private var _isQueueClosed = MutableStateFlow(true)
    private var _isQueueSingleEvent = MutableStateFlow(true)

    init {
        getErrorFlow()
        getQueuesData()
    }

    private fun getErrorFlow(){
        viewModelScope.launch {
            repository.getErrorFlow().collect{
                it.message?.let { it1 -> _errorText.emit(it1) }
            }
        }
    }

    private fun getQueuesData(){
        viewModelScope.launch {
            repository.getQueues().collect{
                _myQueues.value = it.first
                _queues.value = it.second
            }
        }
    }

    fun changeQueueClosed(isClosed: Boolean) {
        _isQueueClosed.value = isClosed
    }

    fun changeQueueSingleEvent(isSingleEvent: Boolean) {
        _isQueueSingleEvent.value = isSingleEvent
    }

    fun createQueue(title: String, description: String) {
        _showLoading.value = true
        viewModelScope.launch {
            val res = repository.createQueue(title, description, _isQueueClosed.value)
            if (res.isSuccess) {
                _showLoading.value = false
                creationComplete.value = true
            } else {
                _showLoading.value = false
                creationComplete.value = true
            }
        }
    }


}

data class QueueItem(val title: String, val description: String, val members: Int)