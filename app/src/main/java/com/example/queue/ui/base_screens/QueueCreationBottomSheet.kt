package com.example.queue.ui.base_screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.queue.R
import com.example.queue.ui.components.TopLoadingBar
import com.example.queue.ui.viewmodel.QueuesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueCreationBottomSheet(viewModel: QueuesViewModel, onExitClicked: () -> Unit) {
    var queueName by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    val isLoading = viewModel.showLoading.collectAsState()

    ModalBottomSheet(onDismissRequest = onExitClicked) {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Создание очереди",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onExitClicked) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.arrow_back),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.createQueue(queueName, description)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.check),
                            contentDescription = "Save"
                        )
                    }
                }
            )
        }, modifier = Modifier.wrapContentHeight()) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
            ) {
                TopLoadingBar(isLoading = isLoading.value)
                MyTextField(hint = "Название очереди", dataText = queueName) { queueName = it }
                MyTextField(hint = "Описание", dataText = description, lines = 5) {
                    description = it
                }
            }
        }
    }


}

@Composable
fun MyTextField(hint: String, dataText: String, lines: Int = 1, onTextChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isTextFieldFocused by interactionSource.collectIsFocusedAsState()

    OutlinedTextField(
        value = dataText,
        onValueChange = { onTextChange(it) },
        label = { Text(hint) },
        maxLines = lines,
        trailingIcon = {
            AnimatedVisibility(
                visible = isTextFieldFocused && dataText.isNotEmpty(),
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                IconButton(onClick = { onTextChange("") }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.clear),
                        contentDescription = "Clear"
                    )
                }
            }
        },
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}