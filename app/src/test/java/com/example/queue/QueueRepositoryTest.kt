package com.example.queue

import com.example.queue.data.entities.Queue
import com.example.queue.data.firebase.QueueFirestoreDB
import com.example.queue.domain.repositories.QueueRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.Mock
import kotlin.test.Test

class QueueRepositoryTest {

    @Mock
    private lateinit var queueFirestoreDB: QueueFirestoreDB

    private lateinit var queueRepository: QueueRepository

    @Before
    fun setUp() {
        queueFirestoreDB = mockk()
        queueRepository = QueueRepository(queueFirestoreDB)
    }


    @Test
    fun `test getQueues calls FirestoreDB`() = runTest {
        val testFlow = flowOf(Pair(emptyList<Queue>(), emptyList<Queue>()))
        coEvery { queueFirestoreDB.getQueues() } returns testFlow

        val result = queueRepository.getQueues()

        coVerify { queueFirestoreDB.getQueues() }
        result.collect { value ->
            assert(value == Pair(emptyList<Queue>(), emptyList<Queue>()))
        }
    }
}