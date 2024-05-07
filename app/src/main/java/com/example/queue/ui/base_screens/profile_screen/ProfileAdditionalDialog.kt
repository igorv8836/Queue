package com.example.queue.ui.base_screens.profile_screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.DialogProperties

@Composable
fun ProfileAdditionalDialog(
    title: String, textFieldLabel: String, denyRequest: () -> Unit, confirmRequest: (String) -> Unit
) {
    var textFieldText by remember { mutableStateOf("") }

    AlertDialog(onDismissRequest = denyRequest, title = {
        Text(text = title)
    }, text = {
        OutlinedTextField(value = textFieldText,
            onValueChange = { textFieldText = it },
            label = { Text(textFieldLabel) })
    }, confirmButton = {
        TextButton(onClick = { confirmRequest(textFieldText) }) {
            Text(text = "Сохранить")
        }
    }, dismissButton = {
        TextButton(onClick = denyRequest) {
            Text(text = "Отмена")
        }
    }, properties = DialogProperties(usePlatformDefaultWidth = false)
    )
}