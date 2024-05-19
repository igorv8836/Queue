package com.example.queue.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.data.entities.Queue
import com.example.queue.data.firebase.QueueFirestoreDB
import com.example.queue.domain.repositories.QueueRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QueueViewModel : ViewModel() {
    private val queueRepository = QueueRepository(QueueFirestoreDB())
    private val _queue = MutableStateFlow(Queue.getEmptyQueue())
    val queue = _queue.asStateFlow()

    private val _userId = MutableStateFlow<String?>(null)
    val userId = _userId.asStateFlow()
    private val _isDeleted = MutableStateFlow(false)
    val isDeleted = _isDeleted.asStateFlow()
    private val _helpingText = MutableStateFlow<String?>(null)
    val helpingText = _helpingText.asStateFlow()

    private var startIndex: Int? = null

    init {
        checkError()
        getCurrentUser()
    }

    fun getQueue(id: String) {
        viewModelScope.launch {
            queueRepository.getQueue(id).collect {
                _queue.value = it ?: Queue.getEmptyQueue()
                Log.i("tag", it?.owner?.isActive.toString() + "fd")
            }
        }

    }

    private fun checkError() {
        viewModelScope.launch {
            queueRepository.getErrorFlow().collect {
                _helpingText.value = it.message
            }
        }
    }

    private fun getCurrentUser() {
        _userId.value = queueRepository.getCurrentUser()
    }


    fun deleteQueue() {
        viewModelScope.launch {
            val res = queue.value.id?.let { queueRepository.deleteQueue(it) }
            res?.let {
                if (it.isSuccess)
                    _isDeleted.value = it.getOrNull() ?: false
            }
        }


    }

    fun exitFromQueue() {
        viewModelScope.launch {
            val res = queue.value.id?.let { queueRepository.exitFromQueue(it) }
            if (res?.isSuccess == true) {
                _isDeleted.value = res.getOrNull() ?: false
            }
        }
    }

     fun deleteUser(id: String){
         viewModelScope.launch {
            queueRepository.exitFromQueue(id)
        }
     }

    fun restartQueue() {
        viewModelScope.launch {
            queue.value.id?.let { queueRepository.restartQueue(it) }
        }
    }

    fun competeActionInQueue(userId: String) {
        viewModelScope.launch {
            queue.value.id?.let {
                queueRepository.completeActionInQueue(it, userId)
                if (queue.value.members.size >= 2 && userId == queue.value.members[0].id)
                    queueRepository.sendNotification(queue.value.members[1].id, queue.value.name)
            }
        }
    }

    fun startQueue() {
        viewModelScope.launch {
            queue.value.let { queue ->
                queue.id?.let { queueRepository.changeIsStarted(it, !queue.isStarted) }
            }
        }
    }

    fun sendInvitation(nickname: String) {
        viewModelScope.launch {
            queue.value.id?.let {
                val res = queueRepository.sendInvitation(it, nickname)
                if (res.isSuccess)
                    _helpingText.value = "Приглашение отправлено"
            }
        }
    }

    fun moveMember(fromIndex: Int, toIndex: Int) {
        viewModelScope.launch {
            if (startIndex == null)
                startIndex = fromIndex

            if (startIndex!! <= toIndex || (userId.value == queue.value.owner.id)) {
                val mutableMembers = _queue.value.members.toMutableList()
                mutableMembers.add(toIndex, mutableMembers.removeAt(fromIndex))
                _queue.value = _queue.value.copy(members = mutableMembers)
            }
        }
    }

    fun endMoving(fromIndex: Int, toIndex: Int) {
        viewModelScope.launch {
            startIndex = null
            queueRepository.changePosition(
                queue.value.id ?: "",
                queue.value.members[toIndex].id,
                toIndex
            )
        }
    }
}