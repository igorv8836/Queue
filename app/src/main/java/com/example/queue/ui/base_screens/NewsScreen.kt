package com.example.queue.ui.base_screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.queue.ui.Dimens
import com.example.queue.ui.components.LoadingScreen
import com.example.queue.ui.viewmodel.NewsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val newsData by viewModel.newsData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        LoadingScreen()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = Dimens.small, end = Dimens.small)
        ) {
            items(newsData) { news ->
                    NewsItem(
                        title = news.title,
                        description = news.text,
                        date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                            .format(news.date)
                    )
            }
        }
    }
}


@Composable
fun NewsItem(title: String, description: String, date: String) {
    Surface(
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(top = Dimens.medium),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.medium)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}
