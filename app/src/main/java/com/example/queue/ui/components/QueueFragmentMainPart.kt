package com.example.queue.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.queue.R
import com.example.queue.viewmodels.QueueViewModel

@Composable
fun QueueFragmentMainPart(viewModel: QueueViewModel) {
    val queue by viewModel.queue.collectAsState()
    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = queue?.name ?: "",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = queue?.members?.size?.let { getCountText(it) } ?: "",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
            )

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                Column {
                    Text(
                        text = "Описание",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(start = 8.dp, top = 8.dp),
                    )
                    Text(
                        text = queue?.description ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                    )
                    HorizontalDivider(modifier = Modifier.padding(start = 8.dp, end = 8.dp))
                    Text(
                        text = "Создатель",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(top = 4.dp, start = 8.dp),
                    )
                    Text(
                        text = queue?.owner?.name ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                    )
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ManagingButton(
                    iconResource = R.drawable.start_icon,
                    text = "Запустить",
                    isAbled = true,
                    onClick = {}
                )
                ManagingButton(
                    iconResource = R.drawable.restart_icon,
                    text = "Перезапустить",
                    isAbled = true,
                    onClick = {}
                )
                ManagingButton(
                    iconResource = R.drawable.exit_icon,
                    text = "Покинуть",
                    isAbled = true,
                    onClick = {}
                )
                ManagingButton(
                    iconResource = R.drawable.delete_icon,
                    text = "Удалить",
                    isAbled = false,
                    onClick = {}
                )
            }
        }
    }
}

fun getCountText(num: Int) =
    when (num){
        1 -> "$num участник"
        in 2..4 -> "$num участника"
        else -> "$num участников"
    }