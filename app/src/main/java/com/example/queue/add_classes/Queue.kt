package com.example.queue.add_classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Queue(
    val id: String? = null,
    val name: String,
    val description: String,
    val members: List<Member>,
    val isStarted: Boolean,
    val owner: Member
)
