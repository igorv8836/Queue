package com.example.queue.viewmodels

import androidx.lifecycle.ViewModel
import com.example.queue.add_classes.Member
import com.example.queue.add_classes.Queue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class QueueViewModel: ViewModel() {
    private val _queue = MutableStateFlow<Queue?>(null)
    val queue = _queue.asStateFlow()

    fun setReceivedQueue(queue: Queue){
        _queue.value = queue
    }

    init {
        _queue.value = Queue(
            "1",
            "Queue 1",
            "Description 1",
            listOf(
                Member("1", "User 1", true, "fa", 0),
                Member("1", "User 1", true, "fa", 0),
                Member("1", "User 1", true, "fa", 0)
            ),
            true,
            true,
            Member("1", "User 1", true, "fa", 0)
        )
    }
}