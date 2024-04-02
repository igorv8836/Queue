package com.example.queue.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.example.queue.R
import com.example.queue.viewmodels.QueueCreationViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreationQueueFragment : BottomSheetDialogFragment() {
    private val viewModel: QueueCreationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    QueueCreationScreen(viewModel) { dismiss() }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueCreationScreen(viewModel: QueueCreationViewModel, onExitClicked: () -> Unit) {
    var queueName by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Создание очереди", style = MaterialTheme.typography.titleLarge) },
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
                    onExitClicked()
                }) {
                    Icon(painter = painterResource(id = R.drawable.check), contentDescription = "Save")
                }
            }
        )
    }, modifier = Modifier.wrapContentHeight()) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            MyTextField(hint = "Название очереди", dataText = queueName){ queueName = it }
            MyTextField(hint = "Описание", dataText = description, lines = 5){ description = it }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(300.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QueueStatus("Закрытая", "Открытая", viewModel::changeQueueClosed)
                QueueStatus("Одноразовая", "Периодическая", viewModel::changeQueueSingleEvent)
            }
        }
    }
}

@Composable
fun MyTextField(hint: String, dataText: String, lines: Int = 1, onTextChange: (String) -> Unit){
    val interactionSource = remember { MutableInteractionSource() }
    val isTextFieldFocused by interactionSource.collectIsFocusedAsState()

    TextField(
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

@Composable
fun QueueStatus(ableText: String, unableText: String, updateStatus: (Boolean) -> Unit){
    val isUnable = rememberSaveable() { mutableStateOf(false) }
    val surfaceColor by animateColorAsState(
        if (isUnable.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
        label = ""
    )
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        shadowElevation = 4.dp,
        color = surfaceColor,
        modifier = Modifier
            .animateContentSize()
            .padding(8.dp)
            .clickable {
                isUnable.value = !isUnable.value
                updateStatus(isUnable.value)
            }
    ){
        Text(
            text = if (isUnable.value) unableText else ableText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview
@Composable
fun PreviewMyTopAppBar() {
    MaterialTheme {
        QueueCreationScreen(QueueCreationViewModel()) {}
    }
}