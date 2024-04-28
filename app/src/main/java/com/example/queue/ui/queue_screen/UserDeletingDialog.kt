package com.example.queue.ui.queue_screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.example.queue.viewmodel.QueueViewModel

@Composable
fun UserDeletingDialog(viewModel: QueueViewModel, showDeletingDialog: MutableState<Boolean>, userId: String) {
    if (showDeletingDialog.value) {
        AlertDialog(title = { Text(text = "Подтвердите удаление") },
            text = { Text(text = "Вы действительно хотите участника с очереди?") },
            onDismissRequest = { showDeletingDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    showDeletingDialog.value = false
                    viewModel.deleteUser(userId)
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
}