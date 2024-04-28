package com.example.queue.ui.base_screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.queue.ui.components.QueueItemCard
import com.example.queue.ui.components.pagerTabIndicatorOffset
import com.example.queue.ui.navigation.RouteName
import com.example.queue.ui.queue_screen.draggable_list.ListItem
import com.example.queue.viewmodel.QueuesViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun QueuesScreen(
    viewModel: QueuesViewModel,
    navController: NavController
) {
    val tabList = listOf("Мои", "Остальные")
    val pagerState = rememberPagerState()
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    val myQueues by viewModel.myQueues.collectAsState()
    val queues by viewModel.queues.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showCreationSheet by remember { mutableStateOf(false) }

    coroutineScope.launch {
        viewModel.creationComplete.collect{
            if (it)
                showCreationSheet = false
        }
    }

    if (showCreationSheet){
        QueueCreationBottomSheet(viewModel = viewModel) {
            showCreationSheet = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    showCreationSheet = true
                }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Queue")
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) {
            Column(modifier = Modifier.padding(it)) {
                TabRow(
                    selectedTabIndex = tabIndex,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
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
                    modifier = Modifier.fillMaxSize()
                ) { index ->
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        when (index) {
                            0 -> items(myQueues) { it1 ->
                                QueueItemCard(it1) {
                                    navController.navigate("${RouteName.QUEUE_SCREEN.value}/${it1.id}")
                                }
                            }

                            1 -> items(queues) { it1 ->
                                QueueItemCard(it1) {
                                    navController.navigate("${RouteName.QUEUE_SCREEN.value}/${it1.id}")
                                }
                            }
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