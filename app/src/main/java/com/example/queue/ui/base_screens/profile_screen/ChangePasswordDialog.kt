package com.example.queue.ui.base_screens.profile_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.DialogProperties

@Composable
fun ChangePasswordDialog(
    denyRequest: () -> Unit, confirmRequest: (String, String) -> Unit
) {
    var lastPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    AlertDialog(onDismissRequest = denyRequest, title = {
        Text(text = "Изменение пароля")
    }, text = {
        Column {
            OutlinedTextField(value = lastPassword,
                onValueChange = { lastPassword = it },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                label = { Text("Введите старый пароль") })
            OutlinedTextField(value = newPassword,
                onValueChange = { newPassword = it },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                label = { Text("Введите новый пароль") })
        }
    }, confirmButton = {
        TextButton(onClick = { confirmRequest(lastPassword, newPassword) }) {
            Text(text = "Сохранить")
        }
    }, dismissButton = {
        TextButton(onClick = denyRequest) {
            Text(text = "Отмена")
        }
    }, properties = DialogProperties(usePlatformDefaultWidth = false)
    )
}