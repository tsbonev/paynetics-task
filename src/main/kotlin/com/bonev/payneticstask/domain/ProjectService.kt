package com.bonev.payneticstask.domain

import java.time.Duration
import java.util.Optional
import java.util.UUID

interface ProjectService {

    fun createProject(request: CreateProjectRequest): Project
    fun getProjectById(projectId: UUID): Optional<Project>

    fun getProjectsByPage(page: Int, pageSize: Int): List<Project>

    fun updateProject(request: UpdateProjectRequest): Project

    fun updateProjectStatus(request: UpdateProjectStatusRequest): Project

    fun deleteProject(projectId: UUID): Project

    fun createTask(projectId: UUID, taskRequest: CreateTaskRequest): Task

    fun updateTask(request: UpdateTaskRequest): Task

    fun updateTaskStatus(request: UpdateTaskStatusRequest): Task

    fun deleteTask(taskId: UUID): Task
}

data class CreateProjectRequest(
        val title: String,
        val description: String,
        val company: String?,
        val client: String?
)

data class UpdateProjectRequest(
        val projectId: UUID,
        val title: String,
        val description: String,
        val company: String?,
        val client: String?
)

data class UpdateProjectStatusRequest(
        val projectId: UUID,
        val newStatus: ProjectStatus,
)

data class CreateTaskRequest(
        val title: String,
        val description: String,
        val duration: Duration
)

data class UpdateTaskRequest(
        val taskId: UUID,
        val title: String,
        val description: String,
        val duration: Duration
)

data class UpdateTaskStatusRequest(
        val taskId: UUID,
        val newStatus: TaskStatus
)

class ProjectNotFoundException(message: String) : RuntimeException(message)
class ProjectStatusChangeException(message: String) : RuntimeException(message)

class TaskNotFoundException(message: String): RuntimeException(message)
class TaskStatusChangeException(message: String): RuntimeException(message)