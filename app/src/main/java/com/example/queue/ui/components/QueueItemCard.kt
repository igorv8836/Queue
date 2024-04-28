package com.example.queue.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.queue.R
import com.example.queue.add_classes.Member
import com.example.queue.add_classes.Queue

@Composable
fun QueueItemCard(queue: Queue, onClick: (Queue) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val ripple = rememberRipple(bounded = true)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple,
                onClick = { onClick(queue) }
            )
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(8.dp, 8.dp, 0.dp, 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = queue.name, style = MaterialTheme.typography.titleLarge,
                    maxLines = 1, modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = queue.description, style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2, overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Участников: ${queue.members.size}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Image(
                painter = painterResource(id = R.drawable.arrow_right),
                contentDescription = "arrow right",
                modifier = Modifier.align(Alignment.CenterVertically)
            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun QueueItemCardPreview() {
    val a = Member(
        "12",
        "name",
        true,
        "path",
        true
    )
    QueueItemCard(
        Queue(
            "11",
            "name",
            "description",
            listOf(a, a, a),
            true,
            a
        )
    ) {}
}