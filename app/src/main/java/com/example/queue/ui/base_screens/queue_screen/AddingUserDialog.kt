package com.example.queue.ui.base_screens.queue_screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.DialogProperties
import com.example.queue.ui.viewmodel.QueueViewModel

@Composable
fun AddingUserDialog(viewModel: QueueViewModel, showingAddingDialog: MutableState<Boolean>) {
    var nickname by remember { mutableStateOf("") }

    if (showingAddingDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showingAddingDialog.value = false
            },
            title = {
                Text(text = "Добавить пользователя")
            },
            text = {
                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Введите никнейм") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showingAddingDialog.value = false
                    viewModel.sendInvitation(nickname)
                }) {
                    Text(text = "Отправить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showingAddingDialog.value = false }) {
                    Text(text = "Отмена")
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }
}