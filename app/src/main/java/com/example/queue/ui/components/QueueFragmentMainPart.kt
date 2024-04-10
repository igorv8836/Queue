package com.example.queue.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.queue.R
import com.example.queue.viewmodels.QueueViewModel

@Composable
fun QueueFragmentMainPart(viewModel: QueueViewModel) {
    val queue by viewModel.queue.collectAsState()
    val userId by viewModel.userId.collectAsState()
    val showDeletingDialog = remember { mutableStateOf(false) }
    val showExitingDialog = remember { mutableStateOf(false) }

    if (showDeletingDialog.value) {
        DeletingAlertDialog(viewModel, showDeletingDialog)
    }

    if (showExitingDialog.value) {
        ExitingAlertDialog(viewModel, showExitingDialog)
    }

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

            Text(text = queue?.members?.size?.let { getCountText(it) } ?: "",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp))

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
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val isAbled = (userId == queue?.owner?.id && userId != null)
                ManagingButton(
                    iconResource = if (queue?.isStarted == true)
                        R.drawable.start_icon
                    else
                        R.drawable.stop_icon,
                    text = if (queue?.isStarted == true)
                        "Запустить"
                    else
                        "Остановить",
                    isAbled = isAbled,
                    color = if (queue?.isStarted == true)
                        MaterialTheme.colorScheme.primary
                    else
                        Color.Red,
                    onClick = { viewModel.startQueue() })
                ManagingButton(iconResource = R.drawable.restart_icon,
                    text = "Перезапустить",
                    isAbled = isAbled,
                    onClick = {})
                ManagingButton(iconResource = R.drawable.exit_icon,
                    text = "Покинуть",
                    isAbled = !isAbled,
                    onClick = { showExitingDialog.value = true })
                ManagingButton(iconResource = R.drawable.delete_icon,
                    text = "Удалить",
                    isAbled = isAbled,
                    onClick = { showDeletingDialog.value = true })
            }
        }
    }
}

@Composable
fun DeletingAlertDialog(viewModel: QueueViewModel, showDeletingDialog: MutableState<Boolean>) {
    AlertDialog(
        title = { Text(text = "Подтвердите удаление") },
        text = { Text(text = "Вы действительно хотите удалить очередь?") },
        onDismissRequest = { showDeletingDialog.value = false },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                onClick = {
                    viewModel.deleteQueue()
                    showDeletingDialog.value = false
                }
            ) {
                Text(text = "Удалить", color = Color.Red)
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                onClick = { showDeletingDialog.value = false }
            ) {
                Text(text = "Отмена", color = MaterialTheme.colorScheme.primary)
            }
        }
    )
}

@Composable
fun ExitingAlertDialog(viewModel: QueueViewModel, showDeletingDialog: MutableState<Boolean>) {
    AlertDialog(
        title = { Text(text = "Подтвердите выход") },
        text = { Text(text = "Вы действительно хотите выйти из очереди?") },
        onDismissRequest = { showDeletingDialog.value = false },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                onClick = {
                    viewModel.exitFromQueue()
                    showDeletingDialog.value = false
                }
            ) {
                Text(text = "Выйти", color = Color.Red)
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                onClick = { showDeletingDialog.value = false }
            ) {
                Text(text = "Отмена", color = MaterialTheme.colorScheme.primary)
            }
        }
    )
}

fun getCountText(num: Int) = when (num) {
    1 -> "$num участник"
    in 2..4 -> "$num участника"
    else -> "$num участников"
}