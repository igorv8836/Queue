package com.example.queue.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.queue.testingData.PreviewQueuesViewModel
import com.example.queue.ui.components.QueueItemCard
import com.example.queue.viewmodels.QueuesViewModel


class QueuesFragment : Fragment() {
    private lateinit var viewModel: QueuesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[QueuesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme() {
                    QueuesScreen(viewModel, parentFragmentManager)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

@Composable
fun QueuesScreen(viewModel: QueuesViewModel, fragmentManager: FragmentManager) {
    val queues by viewModel.queues.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Column(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    CreationQueueFragment().show(
                        fragmentManager,
                        "CreationQueueFragment"
                    )
                }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Queue")
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                items(queues) { queue ->
                    QueueItemCard(queue)
                }
            }
        }

        LaunchedEffect(key1 = true) {
            viewModel.errorText.collect{
                snackbarHostState.showSnackbar(it)
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun QueuesScreenPreview() {
    QueuesScreen(
        viewModel = PreviewQueuesViewModel(),
        fragmentManager = object : FragmentManager() {})
}