package com.example.queue.ui.navigation.bottom_navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigation(navController: NavController?) {
    val backStackEntry = navController?.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.value?.destination?.route
    val listItems = listOf(
        BottomItem.Screen1,
        BottomItem.Screen2,
        BottomItem.Screen3
    )

    NavigationBar() {
        listItems.forEachIndexed { index, item ->
            val interactionSource = remember { MutableInteractionSource() }
            val isSelected = item.route == currentRoute
            val iconColor = animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                label = ""
            )
            NavigationBarItem(
                selected = isSelected,
                onClick = { navController?.navigate(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = "icon",
                        tint = iconColor.value,
                        modifier = Modifier.size(36.dp)
                    )
                },
                label = {
                    AnimatedVisibility(
                        visible = isSelected,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Text(text = item.title)
                    }
                },
                interactionSource = interactionSource,
            )
        }
    }
}