package com.bonev.payneticstask.application

import com.bonev.payneticstask.domain.*
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class ProjectServiceImpl(
        private val projectRepository: ProjectRepository,
        private val taskRepository: TaskRepository,
        private val idGenerator: IdGenerator) : ProjectService {
    override fun createProject(request: CreateProjectRequest): Project {
        val project = Project(idGenerator.generateUUID(), request.title, request.description,
                ProjectStatus.NEW, request.company, request.client,
                listOf())

        return projectRepository.save(project)
    }

    override fun getProjectById(projectId: UUID): Optional<Project> {
        return projectRepository.get(projectId)
    }

    override fun updateProject(request: UpdateProjectRequest): Project {
        val project = projectRepository.get(request.projectId).getOrElse { throw ProjectNotFoundException("Project with id ${request.projectId} not found") }

        val updatedProject = project.copy(title = request.title, description = request.description, client = request.client, company = request.company)

        return projectRepository.save(updatedProject)
    }

    override fun updateProjectStatus(request: UpdateProjectStatusRequest): Project {
        if (request.newStatus == ProjectStatus.DELETED) throw ProjectStatusChangeException("Cannot update project status to DELETED")

        val project = projectRepository.get(request.projectId).getOrElse { throw ProjectNotFoundException("Project with id ${request.projectId} not found") }

        val updatedProject = project.copy(status = request.newStatus)

        return projectRepository.save(updatedProject)
    }

    override fun deleteProject(projectId: UUID): Project {
        val project = projectRepository.get(projectId).getOrElse { throw ProjectNotFoundException("Project with id $projectId not found") }

        if (project.tasks.all { it.status == TaskStatus.DELETED || it.status == TaskStatus.DONE }) {
            val updatedProject = project.copy(status = ProjectStatus.DELETED)

            return projectRepository.save(updatedProject)
        } else throw ProjectStatusChangeException("Cannot delete project with NEW or PENDING tasks")
    }

    override fun createTask(projectId: UUID, taskRequest: CreateTaskRequest): Task {
        val project = projectRepository.get(projectId).getOrElse { throw ProjectNotFoundException("Project with id $projectId not found") }

        val task = Task(idGenerator.generateUUID(), taskRequest.title, taskRequest.description, TaskStatus.NEW, taskRequest.duration)

        val updateProject = project.copy(tasks = project.tasks.plus(task))

        projectRepository.save(updateProject)

        return task
    }

    override fun updateTask(request: UpdateTaskRequest): Task {
        val task = taskRepository.get(request.taskId).getOrElse { throw TaskNotFoundException("Task with id ${request.taskId} not found") }

        val updatedTask = task.copy(title = request.title, description = request.description, duration = request.duration)

        return taskRepository.save(updatedTask)
    }

    override fun updateTaskStatus(request: UpdateTaskStatusRequest): Task {
        if (request.newStatus == TaskStatus.DELETED) throw TaskStatusChangeException("Cannot update task status to DELETED")

        val task = taskRepository.get(request.taskId).getOrElse { throw TaskNotFoundException("Task with id ${request.taskId} not found") }

        val updateTask = task.copy(status = request.newStatus)

        return taskRepository.save(updateTask)
    }

    override fun deleteTask(taskId: UUID): Task {
        val task = taskRepository.get(taskId).getOrElse { throw TaskNotFoundException("Task with id $taskId not found") }

        val updatedTask = task.copy(status = TaskStatus.DELETED)

        return taskRepository.save(updatedTask)
    }
}