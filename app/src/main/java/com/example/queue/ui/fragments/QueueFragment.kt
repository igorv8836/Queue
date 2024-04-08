package com.example.queue.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.queue.add_classes.Queue
import com.example.queue.ui.components.QueueFragmentMainPart
import com.example.queue.ui.components.QueueFragmentMembers
import com.example.queue.viewmodels.QueueViewModel


class QueueFragment : Fragment() {
    private lateinit var viewModel: QueueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[QueueViewModel::class.java]
        arguments?.getParcelable<Queue>("queue")?.let {
            viewModel.setReceivedQueue(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                QueueScreen(viewModel)
            }
        }
    }
}

@Composable
fun QueueScreen(viewModel: QueueViewModel) {
    Column {
        QueueFragmentMainPart(viewModel = viewModel)
        QueueFragmentMembers(viewModel = viewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQueueScreen() {
    val queueViewModel = QueueViewModel()
    QueueScreen(queueViewModel)
}