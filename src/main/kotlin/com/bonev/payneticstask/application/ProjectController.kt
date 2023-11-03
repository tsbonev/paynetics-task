package com.bonev.payneticstask.application

import com.bonev.payneticstask.domain.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*

@RestController
class ProjectController(private val projectService: ProjectService) {

    @PostMapping("/project")
    fun createProject(@RequestBody request: CreateProjectRequestDTO): ResponseEntity<ResponseDTO> {
        val createdProject = projectService.createProject(CreateProjectRequest(request.title, request.description, request.company, request.client))

        return ResponseEntity.ok(ResponseDTO(0, createdProject.toDTO(), listOf()))
    }

    @GetMapping("/project/{projectId}")
    fun getProjectById(@PathVariable projectId: UUID): ResponseEntity<ResponseDTO> {
        val project = projectService.getProjectById(projectId)

        if (project.isEmpty) return ResponseEntity.ok(ResponseDTO(-1, null, listOf("Project with id $projectId not found")))

        return ResponseEntity.ok(ResponseDTO(0, project.get().toDTO(), listOf()))
    }

    @GetMapping("/project/paged")
    fun getProjectsPaginated(@RequestParam(defaultValue = "0") page: Int,
                             @RequestParam(defaultValue = "20") size: Int): ResponseEntity<ProjectPageDTO> {

        return ResponseEntity.ok(ProjectPageDTO(page, size, projectService.getProjectsByPage(page, size).map { it.toDTO() }))
    }


    @PatchMapping("/project/{projectId}")
    fun updateProject(@PathVariable projectId: UUID, @RequestBody request: UpdateProjectRequestDTO): ResponseEntity<ResponseDTO> {
        val project = projectService.updateProject(UpdateProjectRequest(projectId, request.title, request.description, request.company, request.client))

        return ResponseEntity.ok(ResponseDTO(0, project.toDTO(), listOf()))
    }


    @PatchMapping("/project/{projectId}/status")
    fun updateProjectStatus(@PathVariable projectId: UUID, @RequestBody request: UpdateProjectStatusRequestDTO): ResponseEntity<ResponseDTO> {
        val project = projectService.updateProjectStatus(UpdateProjectStatusRequest(projectId, request.newStatus))

        return ResponseEntity.ok(ResponseDTO(0, project.toDTO(), listOf()))
    }

    @DeleteMapping("/project/{projectId}")
    fun deleteProject(@PathVariable projectId: UUID): ResponseEntity<ResponseDTO> {
        val project = projectService.deleteProject(projectId)

        return ResponseEntity.ok(ResponseDTO(0, project.toDTO(), listOf()))
    }

    @PostMapping("/project/{projectId}/task")
    fun createTaskForProject(@PathVariable projectId: UUID, @RequestBody request: CreateTaskRequestDTO): ResponseEntity<ResponseDTO> {
        val createdTask = projectService.createTask(projectId, CreateTaskRequest(request.title, request.description, Duration.of(request.durationAmount, request.durationUnit)))

        return ResponseEntity.ok(ResponseDTO(0, createdTask.toDTO(), listOf()))
    }

    @PatchMapping("/task/{taskId}")
    fun updateTask(@PathVariable taskId: UUID, @RequestBody request: UpdateTaskRequestDTO): ResponseEntity<ResponseDTO> {
        val task = projectService.updateTask(UpdateTaskRequest(taskId, request.title, request.description, Duration.of(request.durationAmount, request.durationUnit)))

        return ResponseEntity.ok(ResponseDTO(0, task.toDTO(), listOf()))
    }


    @PatchMapping("/task/{taskId}/status")
    fun updateTaskStatus(@PathVariable taskId: UUID, @RequestBody request: UpdateTaskStatusRequestDTO): ResponseEntity<ResponseDTO> {
        val task = projectService.updateTaskStatus(UpdateTaskStatusRequest(taskId, request.newStatus))

        return ResponseEntity.ok(ResponseDTO(0, task.toDTO(), listOf()))
    }

    @DeleteMapping("/task/{taskId}")
    fun deleteTask(@PathVariable taskId: UUID): ResponseEntity<ResponseDTO> {
        val task = projectService.deleteTask(taskId)

        return ResponseEntity.ok(ResponseDTO(0, task.toDTO(), listOf()))
    }
}

data class ResponseDTO(
        val code: Int,
        val data: Any?,
        val validationErrors: List<String>
)

data class CreateProjectRequestDTO(
        val title: String,
        val description: String,
        val company: String?,
        val client: String?
)

data class CreateTaskRequestDTO(
        val title: String,
        val description: String,
        val durationUnit: ChronoUnit,
        val durationAmount: Long
)

data class UpdateProjectRequestDTO(
        val title: String,
        val description: String,
        val company: String?,
        val client: String?
)

data class UpdateTaskRequestDTO(
        val title: String,
        val description: String,
        val durationUnit: ChronoUnit,
        val durationAmount: Long
)

data class UpdateProjectStatusRequestDTO(
        val newStatus: ProjectStatus,
)

data class UpdateTaskStatusRequestDTO(
        val newStatus: TaskStatus,
)

data class ProjectPageDTO(
        val page: Int,
        val size: Int,
        val projects: List<ProjectDTO>
)

data class ProjectDTO(
        val id: UUID,
        val title: String,
        val description: String,
        val status: ProjectStatus,
        val company: String?,
        val client: String?,
        val duration: Duration?,
        val tasks: List<TaskDTO>
)

data class TaskDTO(
        val id: UUID,
        val title: String,
        val description: String,
        val status: TaskStatus,
        val duration: Duration
)

fun Project.toDTO(): ProjectDTO {
    return ProjectDTO(this.id, this.title, this.description, this.status, this.company, this.client, this.calculateDuration(), this.tasks.map { it.toDTO() })
}

fun Task.toDTO(): TaskDTO {
    return TaskDTO(this.id, this.title, this.description, this.status, this.duration)
}