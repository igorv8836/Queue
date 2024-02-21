package com.example.queue.listeners

import java.lang.Exception

interface TaskCompleteListener {

    fun onSuccessFinished()
    fun onErrorFinished(error: String)
}