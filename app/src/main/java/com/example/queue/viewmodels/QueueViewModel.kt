package com.example.queue.viewmodels

import androidx.lifecycle.ViewModel
import com.example.queue.add_classes.Queue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class QueueViewModel: ViewModel() {
    private val _queue = MutableStateFlow<Queue?>(null)
    val queue = _queue.asStateFlow()
}