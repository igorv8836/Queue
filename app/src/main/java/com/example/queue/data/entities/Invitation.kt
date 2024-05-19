package com.example.queue.data.entities

data class Invitation(
    val id: String,
    val queueName: String,
    val peopleCount: Int,
    val author: String
)