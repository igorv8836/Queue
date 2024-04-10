package com.example.queue.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.queue.R
import com.example.queue.viewmodels.QueueViewModel

@Composable
fun QueueFragmentMembers(viewModel: QueueViewModel) {
    val queue by viewModel.queue.collectAsState()
    val showingAddingDialog = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val indication = rememberRipple(bounded = true)

    if (showingAddingDialog.value){
        AddingUserDialog(viewModel)
    }

    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = indication,
                        onClick = { }
                    ),
            ){
                Icon(
                    painter = painterResource(id = R.drawable.person_add_icon),
                    contentDescription = "add person icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 16.dp, bottom = 8.dp)
                        .size(32.dp)
                )
                Text(
                    text = "Добавить участника",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 16.dp, bottom = 8.dp)
                        .align(Alignment.CenterVertically),
                )
            }
            HorizontalDivider(modifier = Modifier.padding(start = 8.dp, end = 8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                queue?.let {
                    items(it.members) { user ->
                        QueueMember(user = user)
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddingUserDialog(viewModel: QueueViewModel) {

}