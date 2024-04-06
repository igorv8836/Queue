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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.queue.testingData.PreviewQueuesViewModel
import com.example.queue.ui.components.QueueItemCard
import com.example.queue.ui.components.pagerTabIndicatorOffset
import com.example.queue.viewmodels.QueuesViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


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
                MaterialTheme {
                    QueuesScreen(viewModel, parentFragmentManager)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun QueuesScreen(viewModel: QueuesViewModel, fragmentManager: FragmentManager) {
    val tabList = listOf("Мои", "Остальные")
    val pagerState = rememberPagerState()
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
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
            Column {
                TabRow(
                    selectedTabIndex = tabIndex,
                    indicator = { tabPositions ->
                        TabRowDefaults.PrimaryIndicator(
                            Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                        )
                    }
                ) {
                    tabList.forEachIndexed { index, s ->
                        Tab(
                            selected = tabIndex == index,
                            onClick = {
                                   coroutineScope.launch {
                                       pagerState.animateScrollToPage(index)
                                   }
                            },
                            text = { Text(text = s) })
                    }
                }
                HorizontalPager(
                    count = tabList.size,
                    state = pagerState,
                    modifier = Modifier.weight(1f)
                ) {index ->
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(it)
                    ) {
                        items(queues) { queue ->
                            QueueItemCard(queue)
                        }
                    }
                }
            }

        }

        LaunchedEffect(key1 = true) {
            viewModel.errorText.collect {
                snackbarHostState.showSnackbar(it)
            }

        }
    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun QueuesScreenPreview() {
//    QueuesScreen(
//        viewModel = PreviewQueuesViewModel(),
//        fragmentManager = object : FragmentManager() {})
//}