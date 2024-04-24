package com.example.queue.ui.queue_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.queue.ui.navigation.RouteName
import com.example.queue.viewmodel.QueueViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueScreen(queueId: String?, viewModel: QueueViewModel, navController: NavController) {
    queueId?.let { viewModel.getQueue(it) }

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            TopAppBar(title = { }, navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }

    ) {
        Column(modifier = Modifier.padding(it)) {
            QueueFragmentMainPart(viewModel = viewModel)
            QueueFragmentMembers(viewModel = viewModel)
        }
    }
    LaunchedEffect(key1 = true) {
        viewModel.helpingText.collect {
            it?.let { snackbarHostState.showSnackbar(it) }
        }
    }
}