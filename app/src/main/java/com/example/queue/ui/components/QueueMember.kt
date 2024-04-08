package com.example.queue.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.queue.add_classes.Member

@Composable
fun QueueMember(user: Member) {
    Text(
        text = user.name,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(4.dp)
    )
}