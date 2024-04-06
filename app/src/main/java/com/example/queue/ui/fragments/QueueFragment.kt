package com.example.queue.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.queue.add_classes.Queue
import com.example.queue.viewmodels.QueueViewModel


class QueueFragment : Fragment() {
    private lateinit var viewModel: QueueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[QueueViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme{
                    QueueScreen(viewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQueueScreen(){
    MaterialTheme {
        val queueViewModel = QueueViewModel()
        QueueScreen(queueViewModel)
    }
}

@Composable
fun QueueScreen(viewModel: QueueViewModel){
    val queue by viewModel.queue.collectAsState()

    Column {
        queue?.let {
            MainPart(it, viewModel)
            Members(it, viewModel)
        }
    }
}

@Composable
fun MainPart(queue: Queue, viewModel: QueueViewModel){
    Column(modifier = Modifier
        .padding(8.dp)
        .wrapContentHeight()){
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = queue.name,
                style = MaterialTheme.typography.titleLarge,
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
            )
            Text(
                text = queue.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = ("Создатель: " + viewModel.queue.value?.owner) ?: "No owner",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
                ) {
                QueueIndicator(text = if (queue.isOpen) "Открытый" else "Закрытый")
                QueueIndicator(text = if (queue.isPeriodic) "Запущен" else "Не запущен")
            }
            Text(
                text = queue.members.size.toString() + " участников",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp)
                )
        }
    }
}

@Composable
fun Members(queue: Queue, viewModel: QueueViewModel){
    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Button(
                onClick = {},
                modifier = Modifier
                    .wrapContentHeight().fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.Start)){
                Text(text = "Добавить участника")
            }
            LazyColumn(modifier = Modifier.fillMaxSize()){
                items(queue.members){user ->
//                    Member(user)
                }

            }
        }
    }
}

@Composable
fun QueueIndicator(text: String){
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .animateContentSize()
            .padding(8.dp)
    ){
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun Member(user: String){
    Text(
        text = user,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(4.dp)
    )
}