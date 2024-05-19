package com.example.queue

import com.example.queue.data.entities.Member
import com.example.queue.data.entities.Queue
import junit.framework.TestCase.assertEquals
import kotlin.test.Test

class QueueTest {

    @Test
    fun `test Queue entity creation`() {
        val queue = Queue(
            name = "Test Queue",
            description = "Test Description",
            id = null,
            members = emptyList(),
            isStarted = false,
            owner = Member("", "", false, "", true),
            inactiveMembers = emptyList()
        )
        assertEquals("Test Queue", queue.name)
        assertEquals("Test Description", queue.description)
        assertEquals(null, queue.id)
        assertEquals(emptyList<Member>(), queue.members)
        assertEquals(false, queue.isStarted)
        assertEquals(Member("", "", false, "", true), queue.owner)
        assertEquals(emptyList<String>(), queue.inactiveMembers)
    }
}