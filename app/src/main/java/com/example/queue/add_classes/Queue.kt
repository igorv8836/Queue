package com.example.queue.add_classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Queue(
    val id: String? = null,
    val name: String,
    val description: String,
    val members: List<Member>,
    val isOpen: Boolean,
    val isPeriodic: Boolean,
    val owner: Member
) : Parcelable
