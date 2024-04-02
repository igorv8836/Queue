package com.example.queue.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.queue.R
import com.example.queue.viewmodels.QueueItem

@Composable
fun QueueItemCard(queue: QueueItem) {
    val interactionSource = remember { MutableInteractionSource() }
    val ripple = rememberRipple(bounded = true)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple,
                onClick = {}
            )
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row {
            Column(modifier = Modifier
                .padding(16.dp, 16.dp, 0.dp, 16.dp)
                .weight(1f)) {
                Text(text = queue.title, style = MaterialTheme.typography.bodyLarge)
                Text(text = queue.description, style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(text = "Members: ${queue.members}", style = MaterialTheme.typography.bodyMedium)
            }
            Image(painter = painterResource(id = R.drawable.arrow_right),
                contentDescription = "arrow right", modifier = Modifier.align(Alignment.CenterVertically))
        }

    }
}