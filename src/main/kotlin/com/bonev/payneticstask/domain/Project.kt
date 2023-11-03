package com.bonev.payneticstask.domain

import java.time.Duration
import java.util.UUID

data class Project(
        val id: UUID,

        val title: String,
        val description: String,
        val status: ProjectStatus,

        val company: String?,
        val client: String?,

        val tasks: List<Task>
)

enum class ProjectStatus {
    NEW, PENDING, DONE, DELETED
}

data class Task(
        val id: UUID,

        val name: String,
        val description: String,
        val status: TaskStatus,
        val duration: Duration
)

enum class TaskStatus {
    NEW, PENDING, DONE, DELETED
}