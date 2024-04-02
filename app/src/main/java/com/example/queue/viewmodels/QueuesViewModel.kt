package com.example.queue.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.repositories.QueueRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class QueuesViewModel: ViewModel() {
    private val repository = QueueRepository
    private val _queues = MutableStateFlow<List<QueueItem>>(emptyList())
    open val queues = _queues.asStateFlow()
    private val _errorText = MutableSharedFlow<String>()
    val errorText: SharedFlow<String> = _errorText

    init {
        getErrorFlow()
        val initialQueues = listOf(
            QueueItem("Queue 1", "Description 1", 3),
            QueueItem("Queue 2", "Description 2", 5)
        )
        _queues.value = initialQueues
    }

    private fun getErrorFlow(){
        viewModelScope.launch {
            repository.getErrorFlow().collect{
                it.message?.let { it1 -> _errorText.emit(it1) }
            }
        }
    }


}

data class QueueItem(val title: String, val description: String, val members: Int)