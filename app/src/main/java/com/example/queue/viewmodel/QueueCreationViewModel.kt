package com.example.queue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.model.repositories.QueueRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QueueCreationViewModel: ViewModel() {
    private val repository = QueueRepository
    val creationComplete = MutableStateFlow(false)
    private val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading
    private var _isQueueClosed = MutableStateFlow(true)
    private var _isQueueSingleEvent = MutableStateFlow(true)

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