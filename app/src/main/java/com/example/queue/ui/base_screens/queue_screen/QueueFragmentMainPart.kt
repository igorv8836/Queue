package com.example.queue.ui.base_screens.queue_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.queue.R
import com.example.queue.ui.viewmodel.QueueViewModel

@Composable
fun QueueFragmentMainPart(viewModel: QueueViewModel, navController: NavController) {
    val queue by viewModel.queue.collectAsState()
    val userId by viewModel.userId.collectAsState()
    val showDeletingDialog = remember { mutableStateOf(false) }
    val showExitingDialog = remember { mutableStateOf(false) }

    if (showDeletingDialog.value) {
        DeletingAlertDialog(viewModel, showDeletingDialog, navController)
    }

    if (showExitingDialog.value) {
        ExitingAlertDialog(viewModel, showExitingDialog, navController)
    }

    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.weight(1f))
            Text(
                text = queue.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(end = 8.dp),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Box(
                modifier = Modifier
                    .padding()
                    .size(12.dp)
                    .align(Alignment.CenterVertically)
                    .background(
                        color = if (queue.isStarted) Color.Green else Color.Red,
                        shape = MaterialTheme.shapes.small
                    )
            )
            Spacer(Modifier.weight(1f))
        }
        Text(text = queue.members.size.let { getCountText(it) },
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
                    text = queue.description,
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
                    text = queue.owner.name,
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
            val isAbled = (userId == queue.owner.id && userId != null)
            ManagingButton(iconResource = if (!queue.isStarted) R.drawable.start_icon
            else R.drawable.stop_icon,
                text = if (!queue.isStarted) "Запустить"
                else "Остановить",
                isAbled = isAbled,
                color = if (!queue.isStarted) MaterialTheme.colorScheme.primary
                else Color.Red,
                onClick = { viewModel.startQueue() })
            ManagingButton(iconResource = R.drawable.restart_icon,
                text = "Перезапустить",
                isAbled = isAbled,
                onClick = { viewModel.restartQueue() })
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

@Composable
fun DeletingAlertDialog(
    viewModel: QueueViewModel,
    showDeletingDialog: MutableState<Boolean>,
    navController: NavController
) {
    AlertDialog(title = { Text(text = "Подтвердите удаление") },
        text = { Text(text = "Вы действительно хотите удалить очередь?") },
        onDismissRequest = { showDeletingDialog.value = false },
        confirmButton = {
            TextButton(onClick = {
                viewModel.deleteQueue()
                showDeletingDialog.value = false
                navController.popBackStack()
            }) {
                Text(text = "Удалить", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = { showDeletingDialog.value = false }) {
                Text(text = "Отмена")
            }
        })
}

@Composable
fun ExitingAlertDialog(
    viewModel: QueueViewModel,
    showExitingDialog: MutableState<Boolean>,
    navController: NavController
) {
    AlertDialog(title = { Text(text = "Подтвердите выход") },
        text = { Text(text = "Вы действительно хотите выйти из очереди?") },
        onDismissRequest = { showExitingDialog.value = false },
        confirmButton = {
            TextButton(onClick = {
                viewModel.exitFromQueue()
                showExitingDialog.value = false
                navController.popBackStack()
            }) {
                Text(text = "Выйти", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = { showExitingDialog.value = false }) {
                Text(text = "Отмена")
            }
        })
}

fun getCountText(num: Int) = when (num) {
    1 -> "$num участник"
    in 2..4 -> "$num участника"
    else -> "$num участников"
}