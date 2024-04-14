package com.example.queue.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.queue.R
import com.example.queue.viewmodels.QueueViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QueueFragmentMembers(viewModel: QueueViewModel) {
    val queue by viewModel.queue.collectAsState()
    val showingAddingDialog = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val indication = rememberRipple(bounded = true)
    val listState = rememberLazyListState()
    AddingUserDialog(viewModel, showingAddingDialog)

    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = indication,
                        onClick = { showingAddingDialog.value = true }
                    ),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.person_add_icon),
                    contentDescription = "add person icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 16.dp, bottom = 8.dp)
                        .size(32.dp)
                )
                Text(
                    text = "Добавить участника",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 16.dp, bottom = 8.dp)
                        .align(Alignment.CenterVertically),
                )
            }
            HorizontalDivider(modifier = Modifier.padding(start = 8.dp, end = 8.dp))

            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                queue?.let {
                    itemsIndexed(it.members) { _, user ->
                        val dismissState = rememberDismissState()
                        if (dismissState.isDismissed(DismissDirection.EndToStart)) {

                        }

                        SwipeToDismiss(
                            state = dismissState,
                            directions = setOf(DismissDirection.EndToStart),
                            dismissThresholds = { direction ->
                                FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.1f else 0.05f)
                            },
                            background = {
                                val color by animateColorAsState(
                                    when (dismissState.targetValue) {
                                        androidx.compose.material.DismissValue.Default -> MaterialTheme.colorScheme.surface
                                        else -> Color.Green
                                    }, label = ""
                                )
                                val alignment = Alignment.CenterEnd
                                val icon = Icons.Filled.Check

                                val scale by animateFloatAsState(
                                    if (dismissState.targetValue == androidx.compose.material.DismissValue.Default)
                                        0.75f
                                    else
                                        1f,
                                    label = ""
                                )
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = alignment
                                ) {
                                    Icon(
                                        icon,
                                        contentDescription = "Check Icon",
                                        modifier = Modifier.scale(scale)
                                    )
                                }
                            },
                            dismissContent = {
                                Card(
                                    elevation = CardDefaults.cardElevation(animateDpAsState(
                                        if (dismissState.dismissDirection != null) 4.dp else 0.dp,
                                        label = ""
                                    ).value),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    QueueMember(user = user)
                                }
                            }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

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