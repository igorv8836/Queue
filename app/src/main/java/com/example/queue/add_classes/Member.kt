package com.example.queue.add_classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Member(
    val id: String,
    val name: String,
    val isAdmin: Boolean,
    val imagePath: String,
    val position: Int
    ) : Parcelable
