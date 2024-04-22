package com.example.queue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.add_classes.Queue
import com.example.queue.model.repositories.QueueRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class QueuesViewModel: ViewModel() {
    private val repository = QueueRepository
    private val _errorText = MutableSharedFlow<String>()
    val errorText: SharedFlow<String> = _errorText
    private val _queues = MutableStateFlow<List<Queue>>(emptyList())
    open val queues = _queues.asStateFlow()
    private val _myQueues = MutableStateFlow<List<Queue>>(emptyList())
    open val myQueues = _myQueues.asStateFlow()

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


}

data class QueueItem(val title: String, val description: String, val members: Int)