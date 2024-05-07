package com.example.queue.ui.base_screens.profile_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.queue.ui.Dimens

@Composable
fun SettingsItem(text: String, click: () -> Unit = {}) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { click() }
            .padding(top = Dimens.medium, bottom = Dimens.medium, start = Dimens.medium),
        textAlign = TextAlign.Left
    )
    HorizontalDivider(
        color = Color.Gray,
        thickness = 1.dp,
        modifier = Modifier.padding(start = Dimens.small)
    )
}