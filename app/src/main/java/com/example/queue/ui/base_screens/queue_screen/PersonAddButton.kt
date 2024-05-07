package com.example.queue.ui.base_screens.queue_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.queue.R


@Composable
fun PersonAddButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val indication = rememberRipple(bounded = true)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = indication,
                onClick = onClick
            ),
    )
    {
        Icon(
            painter = painterResource(id = R.drawable.person_add_icon),
            contentDescription = "add person icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = 8.dp, top = 16.dp, bottom = 8.dp)
                .size(32.dp)
        )
        Text(
            text = "Добавить участника",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = 8.dp, top = 16.dp, bottom = 8.dp)
                .align(Alignment.CenterVertically),
        )
    }
}