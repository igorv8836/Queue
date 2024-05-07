package com.example.queue.add_classes

data class Queue(
    val id: String? = null,
    val name: String,
    val description: String,
    var members: List<Member>,
    val isStarted: Boolean,
    val owner: Member,
    val inactiveMembers: List<String> = emptyList()
) {
    companion object {
        fun getEmptyQueue() = Queue(
            "",
            "",
            "",
            emptyList(),
            false,
            Member("", "", false, "", true)
        )
    }
}
