package com.example.queue.ui.queue_screen

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.queue.add_classes.Member
import com.example.queue.viewmodel.QueueViewModel
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueFragmentMembers(viewModel: QueueViewModel) {
    val queue by viewModel.queue.collectAsState()
    val userId by viewModel.userId.collectAsState()
    val showingAddingDialog = remember { mutableStateOf(false) }
    val showDeletingDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val state = rememberReorderableLazyListState(
        onMove = { from, to ->
            if (queue.members[from.index].id == userId)
                viewModel.moveMember(from.index, to.index)
        },
        onDragEnd = { from, to ->
            viewModel.endMoving(from, to)
        }
    )

    AddingUserDialog(viewModel, showingAddingDialog)
    UserDeletingDialog(viewModel = viewModel, showDeletingDialog = showDeletingDialog)

    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            PersonAddButton { showingAddingDialog.value = true }
            HorizontalDivider(modifier = Modifier.padding(start = 8.dp, end = 8.dp))

            LazyColumn(
                state = state.listState,
                modifier = Modifier
                    .reorderable(state)
                    .detectReorderAfterLongPress(state)
                    .fillMaxSize()
            ) {
                items(queue.members, { it.id }) { user ->
                    val dismissState = rememberSwipeToDismissBoxState()

                    when (dismissState.currentValue) {
                        SwipeToDismissBoxValue.EndToStart -> {
                            coroutineScope.launch {
                                dismissState.dismiss(SwipeToDismissBoxValue.Settled)
                            }
                        }

                        SwipeToDismissBoxValue.StartToEnd -> {
                            showDeletingDialog.value = true
                            coroutineScope.launch {
                                dismissState.dismiss(SwipeToDismissBoxValue.Settled)
                            }
                        }

                        else -> Unit
                    }
                    if (user.id == userId) {
                        ReorderableItem(state, key = user.id) { isDragging ->
                            SwipeDismissContent(user, dismissState)
                        }
                    } else {
                        SwipeDismissContent(user = user, dismissState = dismissState)
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeDismissContent(user: Member, dismissState: SwipeToDismissBoxState) {
    val (icon, alignment) = when (dismissState.targetValue) {
        SwipeToDismissBoxValue.StartToEnd -> Pair(Icons.Filled.Delete, Alignment.CenterStart)
        SwipeToDismissBoxValue.EndToStart -> Pair(Icons.Filled.Check, Alignment.CenterEnd)
        else -> Pair(Icons.Filled.Check, Alignment.CenterEnd)
    }
    val color by animateColorAsState(
        targetValue = when (dismissState.targetValue) {
            SwipeToDismissBoxValue.EndToStart -> Color.Green
            SwipeToDismissBoxValue.StartToEnd -> Color.Red
            else -> MaterialTheme.colorScheme.surface
        }, label = ""
    )
    val scale by animateFloatAsState(
        if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0.75f else 1f,
        label = ""
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(icon, "Icon", Modifier.scale(scale))
            }
        },
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = true,
        content = {
            QueueMember(user = user)
        }
    )
}