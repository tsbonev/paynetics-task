package com.bonev.payneticstask.domain

import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectServiceImpl(
        private val projectRepository: ProjectRepository,
        private val taskRepository: TaskRepository) : ProjectService {
    override fun createProject(request: CreateProjectRequest): Project {
        TODO("Not yet implemented")
    }

    override fun getProjectById(projectId: UUID): Optional<Project> {
        TODO("Not yet implemented")
    }

    override fun getProjectsByClient(client: String): List<Project> {
        TODO("Not yet implemented")
    }

    override fun getProjectsByCompany(client: String): List<Project> {
        TODO("Not yet implemented")
    }

    override fun updateProject(request: UpdateProjectRequest): Project {
        TODO("Not yet implemented")
    }

    override fun updateProjectStatus(request: UpdateProjectStatusRequest): Project {
        TODO("Not yet implemented")
    }

    override fun deleteProject(projectId: UUID): Project {
        TODO("Not yet implemented")
    }

    override fun createTask(projectId: UUID, taskRequest: CreateTaskRequest): Task {
        TODO("Not yet implemented")
    }

    override fun updateTask(request: UpdateTaskRequest): Task {
        TODO("Not yet implemented")
    }

    override fun updateTaskStatus(request: UpdateTaskStatusRequest): Task {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: UUID): Task {
        TODO("Not yet implemented")
    }
}