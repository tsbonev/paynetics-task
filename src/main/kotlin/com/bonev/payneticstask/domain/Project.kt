package com.bonev.payneticstask.domain

import java.time.Duration
import java.util.*

data class Project(

        val id: UUID = UUID.randomUUID(),

        val title: String,
        val description: String,
        val status: ProjectStatus,

        val company: String?,
        val client: String?,

        val tasks: List<Task>
) {
    init {
        require(!company.isNullOrBlank() || !client.isNullOrBlank()) { "Client or company need to be set" }
    }

    fun calculateDuration(): Duration? {
        if (this.tasks.isEmpty()) return null
        return this.tasks.map { it.duration }.reduce { acc, duration -> acc.plus(duration) }
    }
}

enum class ProjectStatus {
    NEW, PENDING, DONE, DELETED
}

data class Task(
        val id: UUID = UUID.randomUUID(),

        val title: String,
        val description: String,
        val status: TaskStatus,
        val duration: Duration
)

enum class TaskStatus {
    NEW, PENDING, DONE, DELETED
}