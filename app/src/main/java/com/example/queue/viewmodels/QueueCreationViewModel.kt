package com.example.queue.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.repositories.QueueRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QueueCreationViewModel: ViewModel() {
    private val repository = QueueRepository
    private var _isQueueClosed = MutableStateFlow(false)
    private var _isQueueSingleEvent = MutableStateFlow(false)

    private var _helpingMessage = MutableStateFlow("")
    val helpingMessage: StateFlow<String> = _helpingMessage
    fun changeQueueClosed(isClosed: Boolean) {
        _isQueueClosed.value = isClosed
    }

    fun changeQueueSingleEvent(isSingleEvent: Boolean) {
        _isQueueSingleEvent.value = isSingleEvent
    }

    fun createQueue(title: String, description: String) {
        viewModelScope.launch {
            val res = repository.createQueue(title, description, _isQueueClosed.value, _isQueueSingleEvent.value)
            if (res.isFailure)
                _helpingMessage.value = "Ошибка " + res.exceptionOrNull()?.message

        }
    }
}