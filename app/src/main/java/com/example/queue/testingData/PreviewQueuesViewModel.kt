package com.example.queue.testingData

import com.example.queue.add_classes.Queue
import com.example.queue.viewmodels.QueueItem
import com.example.queue.viewmodels.QueuesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreviewQueuesViewModel: QueuesViewModel() {
    private val _queues = MutableStateFlow<List<Queue>>(emptyList())
    override val queues = _queues.asStateFlow()
//    init {
//        _queues.value = listOf(
//            Queue("Queue 1", "Description for Queue 1", 5),
//            Queue("Queue 2", "Description for Queue 2", 15)
//        )
//    }
}