package com.bonev.payneticstask.infrastructure

import com.bonev.payneticstask.domain.Project
import com.bonev.payneticstask.domain.ProjectStatus
import com.bonev.payneticstask.domain.Task
import com.bonev.payneticstask.domain.TaskStatus
import jakarta.persistence.*
import java.time.Duration
import java.util.*

@Entity
data class ProjectEntity(
        @Id
        @GeneratedValue
        val id: UUID = UUID.randomUUID(),

        val title: String,
        val description: String,

        @Enumerated(EnumType.STRING)
        val status: ProjectStatus,

        val company: String?,
        val client: String?,

        @OneToMany
        val tasks: List<TaskEntity>
)

@Entity
data class TaskEntity(
        @Id
        @GeneratedValue
        val id: UUID = UUID.randomUUID(),

        val title: String,
        val description: String,
        @Enumerated(EnumType.STRING)
        val status: TaskStatus,

        val duration: Duration,
)

fun Project.toEntity(): ProjectEntity {
        return ProjectEntity(UUID.randomUUID(), this.title,
                this.description, this.status, this.company, this.client, this.tasks.map { it.toEntity() })
}

fun ProjectEntity.toDomain(): Project {
        return Project(this.id, this.title, this.description, this.status, this.company,
                this.client, this.tasks.map { it.toDomain() })
}

fun Task.toEntity(): TaskEntity {
        return TaskEntity(UUID.randomUUID(), this.title, this.description, this.status, this.duration)
}

fun TaskEntity.toDomain(): Task {
        return Task(this.id, this.title, this.description, this.status, this.duration)
}