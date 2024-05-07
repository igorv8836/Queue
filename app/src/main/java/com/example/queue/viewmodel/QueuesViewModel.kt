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
    private val repository = QueueRepository()
    private val _errorText = MutableSharedFlow<String>()
    val errorText: SharedFlow<String> = _errorText.asSharedFlow()
    private val _queues = MutableStateFlow<List<Queue>>(emptyList())
    val queues = _queues.asStateFlow()
    private val _myQueues = MutableStateFlow<List<Queue>>(emptyList())
    val myQueues = _myQueues.asStateFlow()
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading

    private val _showLoading = MutableStateFlow(false)
    val showLoading = _showLoading.asStateFlow()
    private var _isQueueClosed = MutableStateFlow(true)

    private val _showCreationSheet = MutableStateFlow(false)
    val showCreationSheet: StateFlow<Boolean> = _showCreationSheet

    init {
        getErrorFlow()
        getQueuesData()
    }

    fun showCreationSheet() {
        _showCreationSheet.value = true
    }

    fun hideCreationSheet() {
        _showCreationSheet.value = false
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
                _isLoading.value = false
                _myQueues.value = it.first
                _queues.value = it.second
            }
        }
    }

    fun createQueue(title: String, description: String) {
        _showLoading.value = true
        viewModelScope.launch {
            repository.createQueue(title, description, _isQueueClosed.value).exceptionOrNull()?.let {
                _errorText.emit(it.message ?: "Error")
            }
            _showLoading.value = false
            hideCreationSheet()
        }
    }
}