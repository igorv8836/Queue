package com.example.queue.ui.fragments

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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.queue.R
import com.example.queue.add_classes.Queue
import com.example.queue.ui.components.QueueFragmentMainPart
import com.example.queue.ui.components.QueueFragmentMembers
import com.example.queue.viewmodels.QueueViewModel


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
    Scaffold(topBar = {
        TopAppBar(title = { }, navigationIcon = {
            IconButton(onClick = {
                navController.navigate(R.id.action_to_queuesFragment)
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null
                )
            }
        })
    }) {
        Column(modifier = Modifier.padding(it)) {
            QueueFragmentMainPart(viewModel = viewModel)
            QueueFragmentMembers(viewModel = viewModel)
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun PreviewQueueScreen() {
//    val queueViewModel = QueueViewModel()
//    QueueScreen(
//        queueViewModel
//}