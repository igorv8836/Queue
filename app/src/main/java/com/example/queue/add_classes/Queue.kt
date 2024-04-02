package com.example.queue.add_classes

data class Queue(
    val id: String? = null,
    val name: String,
    val description: String,
    val members: List<String>,
    val isOpen: Boolean,
    val isStarted: Boolean,
    val owner: String
)
