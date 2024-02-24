package com.example.queue.listeners

import com.example.queue.add_classes.NewsItem

interface NewsLoadedListener {
    fun newsLoaded(data: ArrayList<NewsItem>)
    fun newsNotLoaded(error: String)
}