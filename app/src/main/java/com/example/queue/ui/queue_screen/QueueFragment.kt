package com.example.queue.ui.queue_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.queue.R
import com.example.queue.viewmodel.QueueViewModel


class QueueFragment : Fragment() {
    private lateinit var viewModel: QueueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[QueueViewModel::class.java]
        viewModel.getCurrentUser()
        arguments?.getString("queue")?.let { viewModel.getQueue(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val isDeleted by viewModel.isDeleted.collectAsState()
                if (isDeleted)
                    findNavController().navigate(R.id.action_to_queuesFragment)

                QueueScreen(viewModel, findNavController())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueScreen(viewModel: QueueViewModel, navController: NavController) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            TopAppBar(title = { }, navigationIcon = {
                IconButton(onClick = {
                    navController.navigate(R.id.action_to_queuesFragment)
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
    LaunchedEffect(key1 = true){
        viewModel.helpingText.collect{
            it?.let { snackbarHostState.showSnackbar(it) }
        }
    }
}