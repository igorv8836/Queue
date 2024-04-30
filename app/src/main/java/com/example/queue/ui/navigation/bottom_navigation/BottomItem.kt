package com.example.queue.ui.navigation.bottom_navigation

import com.example.queue.R
import com.example.queue.ui.navigation.RouteName

sealed class BottomItem(val title: String, val iconId: Int, val route: String) {
    data object Screen1 : BottomItem("Новости", R.drawable.icon_news, RouteName.NEWS_SCREEN.value)
    data object Screen2 : BottomItem("Очереди", R.drawable.icon_events, RouteName.QUEUES_SCREEN.value)
    data object Screen3 : BottomItem("Профиль", R.drawable.icon_settings, RouteName.PROFILE_SCREEN.value)
}