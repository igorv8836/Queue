package com.example.queue.viewmodels

import androidx.lifecycle.ViewModel
import com.example.queue.add_classes.Queue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class QueueViewModel: ViewModel() {
    private val _queue = MutableStateFlow<Queue?>(null)
    val queue = _queue.asStateFlow()

    fun addTestingData(){
        val a = ArrayList<String>()
        for (i in 0..100){
            a.add("Member $i")
        }
        _queue.value = Queue(
            name = "Очередь тестовая",
            description = "Эта очередь создана для одного хорошего тестирования, а тут я несу просто бред всякий, чтобы было что читать. Вот так вот. Всем удачи! :)",
            members = a,
            owner = "Igorv88361",
            isOpen = true,
            isStarted = false
        )
    }
}