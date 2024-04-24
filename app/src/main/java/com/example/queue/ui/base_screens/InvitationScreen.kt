package com.example.queue.ui.base_screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


data class Invitation(
    val queueName: String,
    val peopleCount: Int,
    val author: String
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InvitationScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Приглашения") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) {
        InvitationList(
            invitations = listOf(
                Invitation("Очередь в стоматологию", 5, "Алексей"),
                Invitation("Очередь в кафе", 3, "Мария"),
                Invitation("Очередь на мойку", 4, "Иван")
            )
        )
    }
}

@Composable
fun InvitationList(invitations: List<Invitation>) {
    LazyColumn {
        items(invitations) { invitation ->
            InvitationItem(invitation)
        }
    }
}

@Composable
fun InvitationItem(invitation: Invitation) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = invitation.queueName,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Количество людей: ${invitation.peopleCount}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Автор: ${invitation.author}",
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { /* Handle decline */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Отклонить")
                }
                Button(
                    onClick = { /* Handle accept */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    Text("Принять")
                }
            }
        }
    }
}
