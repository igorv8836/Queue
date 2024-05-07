package com.example.queue.ui.base_screens.queue_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.queue.add_classes.Member

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun QueueMember(user: Member, isInactive: Boolean = false) {
    val interactionSource = remember { MutableInteractionSource() }
    val indication = rememberRipple(bounded = true)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = indication,
                onClick = { }
            )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            GlideImage(
                model = user.imagePath,
                contentDescription = "user photo",
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically),
                contentScale = ContentScale.FillBounds,
                alpha = if (isInactive) 0.3f else 1f
            )
            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isInactive) Color.LightGray else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp)
                    .align(Alignment.Top)
                    .weight(1f)
            )
            if (user.isAdmin) {
                Text(
                    text = "Создатель",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.Top)
                        .padding(top = 4.dp, end = 8.dp)
                )
            }
        }
    }


}