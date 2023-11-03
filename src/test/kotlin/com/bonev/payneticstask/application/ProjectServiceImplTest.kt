package com.bonev.payneticstask.application

import com.bonev.payneticstask.domain.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.Duration
import java.util.*

@SpringBootTest
class ProjectServiceImplTest {

    @MockBean
    private lateinit var projectRepository: ProjectRepository

    @MockBean
    private lateinit var taskRepository: TaskRepository

    @MockBean
    private lateinit var idGenerator: IdGenerator

    @Autowired
    private lateinit var projectService: ProjectServiceImpl

    @Test
    fun givenValidProject_whenCreateProject_thenWillSaveProject() {
        val uuid = UUID.randomUUID()

        val expectedProject = Project(uuid, "::title::", "::description::", ProjectStatus.NEW, "::company::", "::client::", listOf())

        `when`(idGenerator.generateUUID()).thenReturn(uuid)
        `when`(projectRepository.save(expectedProject))
                .thenReturn(expectedProject)

        projectService.createProject(CreateProjectRequest("::title::", "::description::", "::company::", "::client::"))

        verify(idGenerator).generateUUID()
        verify(projectRepository).save(expectedProject)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenWithNoCompanyProject_whenCreateProject_thenWillSaveProject() {
        val uuid = UUID.randomUUID()

        val expectedProject = Project(uuid, "::title::", "::description::", ProjectStatus.NEW, null, "::client::", listOf())

        `when`(idGenerator.generateUUID()).thenReturn(uuid)
        `when`(projectRepository.save(expectedProject))
                .thenReturn(expectedProject)

        projectService.createProject(CreateProjectRequest("::title::", "::description::", null, "::client::"))

        verify(idGenerator).generateUUID()
        verify(projectRepository).save(expectedProject)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenWithNoClientProject_whenCreateProject_thenWillSaveProject() {
        val uuid = UUID.randomUUID()

        val expectedProject = Project(uuid, "::title::", "::description::", ProjectStatus.NEW, "::company::", null, listOf())

        `when`(idGenerator.generateUUID()).thenReturn(uuid)
        `when`(projectRepository.save(expectedProject))
                .thenReturn(expectedProject)

        projectService.createProject(CreateProjectRequest("::title::", "::description::", "::company::", null))

        verify(idGenerator).generateUUID()
        verify(projectRepository).save(expectedProject)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenInvalidProjectWithNullCompanyAndClient_whenCreateProject_thenWillThrowException() {
        val uuid = UUID.randomUUID()
        `when`(idGenerator.generateUUID()).thenReturn(uuid)

        assertThrows<IllegalArgumentException> { projectService.createProject(CreateProjectRequest("::title::", "::description::", null, null)) }

        verify(idGenerator).generateUUID()
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenInvalidProjectWithBlankCompanyAndClient_whenCreateProject_thenWillThrowException() {
        val uuid = UUID.randomUUID()
        `when`(idGenerator.generateUUID()).thenReturn(uuid)

        assertThrows<IllegalArgumentException> { projectService.createProject(CreateProjectRequest("::title::", "::description::", " ", " ")) }

        verify(idGenerator).generateUUID()
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenProject_whenUpdateValid_thenWillSaveProject() {
        val uuid = UUID.randomUUID()

        val foundProject = Project(uuid, "::title::", "::description::", ProjectStatus.NEW, "::company::", "::client::", listOf())
        val updatedProject = Project(uuid, "::title::", "::updatedDesc::", ProjectStatus.NEW, "::company::", "::client::", listOf())

        `when`(projectRepository.get(uuid)).thenReturn(Optional.of(foundProject))

        `when`(projectRepository.save(updatedProject))
                .thenReturn(updatedProject)

        projectService.updateProject(UpdateProjectRequest(uuid, "::title::", "::updatedDesc::", "::company::", "::client::"))

        verify(projectRepository).get(uuid)
        verify(projectRepository).save(updatedProject)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenNoProject_whenUpdate_thenWillThrowException() {
        val uuid = UUID.randomUUID()

        `when`(projectRepository.get(uuid)).thenReturn(Optional.empty())

        assertThrows<ProjectNotFoundException> {
            projectService.updateProject(UpdateProjectRequest(uuid, "::title::", "::updatedDesc::", "::company::", "::client::"))
        }

        verify(projectRepository).get(uuid)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenProject_whenUpdateNotValid_thenWillThrowException() {
        val uuid = UUID.randomUUID()

        val foundProject = Project(uuid, "::title::", "::description::", ProjectStatus.NEW, "::company::", "::client::", listOf())

        `when`(projectRepository.get(uuid)).thenReturn(Optional.of(foundProject))

        assertThrows<IllegalArgumentException> { projectService.updateProject(UpdateProjectRequest(uuid, "::title::", "::updatedDesc::", "  ", "  ")) }

        verify(projectRepository).get(uuid)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenProject_whenUpdateStatusValid_thenWillSaveProject() {
        val uuid = UUID.randomUUID()

        val foundProject = Project(uuid, "::title::", "::description::", ProjectStatus.NEW, "::company::", "::client::", listOf())
        val updatedProject = Project(uuid, "::title::", "::description::", ProjectStatus.PENDING, "::company::", "::client::", listOf())

        `when`(projectRepository.get(uuid)).thenReturn(Optional.of(foundProject))

        `when`(projectRepository.save(updatedProject))
                .thenReturn(updatedProject)

        projectService.updateProjectStatus(UpdateProjectStatusRequest(uuid, ProjectStatus.PENDING))

        verify(projectRepository).get(uuid)
        verify(projectRepository).save(updatedProject)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenNoProject_whenUpdateStatus_thenWillThrowException() {
        val uuid = UUID.randomUUID()

        `when`(projectRepository.get(uuid)).thenReturn(Optional.empty())

        assertThrows<ProjectNotFoundException> {
            projectService.updateProjectStatus(UpdateProjectStatusRequest(uuid, ProjectStatus.PENDING))
        }

        verify(projectRepository).get(uuid)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenProject_whenUpdateStatusInvalid_thenThrowException() {
        val uuid = UUID.randomUUID()

        val foundProject = Project(uuid, "::title::", "::description::", ProjectStatus.NEW, "::company::", "::client::", listOf())

        `when`(projectRepository.get(uuid)).thenReturn(Optional.of(foundProject))

        assertThrows<ProjectStatusChangeException> { projectService.updateProjectStatus(UpdateProjectStatusRequest(uuid, ProjectStatus.DELETED)) }

        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenProject_whenCreateTask_thenWillSaveProjectAndTask() {
        val uuid = UUID.randomUUID()

        val foundProject = Project(uuid, "::title::", "::description::", ProjectStatus.NEW, "::company::", "::client::", listOf())
        val task = Task(uuid, "::taskTitle::", "::taskDesc::", TaskStatus.NEW, Duration.ofDays(1L))
        val updatedProject = Project(uuid, "::title::", "::description::", ProjectStatus.NEW, "::company::", "::client::", listOf(task))

        `when`(projectRepository.get(uuid)).thenReturn(Optional.of(foundProject))
        `when`(idGenerator.generateUUID()).thenReturn(uuid)
        `when`(projectRepository.save(updatedProject))
                .thenReturn(updatedProject)

        projectService.createTask(uuid, CreateTaskRequest("::taskTitle::", "::taskDesc::", Duration.ofDays(1L)))

        verify(idGenerator).generateUUID()
        verify(projectRepository).get(uuid)
        verify(projectRepository).save(updatedProject)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenNoProject_whenCreateTask_thenWillThrowException() {
        val uuid = UUID.randomUUID()
        `when`(projectRepository.get(uuid)).thenReturn(Optional.empty())

        assertThrows<ProjectNotFoundException> {
            projectService.createTask(uuid, CreateTaskRequest("::taskTitle::", "::taskDesc::", Duration.ofDays(1L)))
        }

        verify(projectRepository).get(uuid)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenProjectWithoutTasks_whenDelete_thenWillMarkAsDeleted() {
        val uuid = UUID.randomUUID()

        val foundProject = Project(uuid, "::title::", "::description::", ProjectStatus.NEW, "::company::", "::client::", listOf())
        val updatedProject = Project(uuid, "::title::", "::description::", ProjectStatus.DELETED, "::company::", "::client::", listOf())

        `when`(projectRepository.get(uuid)).thenReturn(Optional.of(foundProject))
        `when`(projectRepository.save(updatedProject))
                .thenReturn(updatedProject)

        projectService.deleteProject(uuid)

        verify(projectRepository).get(uuid)
        verify(projectRepository).save(updatedProject)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenNoProject_whenDelete_thenWillThrowException() {
        val uuid = UUID.randomUUID()

        `when`(projectRepository.get(uuid)).thenReturn(Optional.empty())

        assertThrows<ProjectNotFoundException> {
            projectService.deleteProject(uuid)
        }

        verify(projectRepository).get(uuid)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenProjectWithDoneOrDeletedTasks_whenDelete_thenWillMarkAsDeleted() {
        val uuid = UUID.randomUUID()

        val doneTask = Task(uuid, "::title::", "::desc::", TaskStatus.DONE, Duration.ofDays(1L))
        val deletedTask = Task(uuid, "::title::", "::desc::", TaskStatus.DELETED, Duration.ofDays(1L))
        val foundProject = Project(uuid, "::title::", "::description::", ProjectStatus.NEW, "::company::", "::client::", listOf(doneTask, deletedTask))
        val updatedProject = Project(uuid, "::title::", "::description::", ProjectStatus.DELETED, "::company::", "::client::", listOf(doneTask, deletedTask))

        `when`(projectRepository.get(uuid)).thenReturn(Optional.of(foundProject))
        `when`(projectRepository.save(updatedProject))
                .thenReturn(updatedProject)

        projectService.deleteProject(uuid)

        verify(projectRepository).get(uuid)
        verify(projectRepository).save(updatedProject)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenProjectWithNewOrPendingTasks_whenDelete_thenWillThrowException() {
        val uuid = UUID.randomUUID()

        val newTask = Task(uuid, "::title::", "::desc::", TaskStatus.NEW, Duration.ofDays(1L))
        val pendingTask = Task(uuid, "::title::", "::desc::", TaskStatus.PENDING, Duration.ofDays(1L))
        val foundProject = Project(uuid, "::title::", "::description::", ProjectStatus.NEW, "::company::", "::client::", listOf(newTask, pendingTask))

        `when`(projectRepository.get(uuid)).thenReturn(Optional.of(foundProject))

        assertThrows<ProjectStatusChangeException> {
            projectService.deleteProject(uuid)
        }

        verify(projectRepository).get(uuid)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenProject_whenGet_thenReturn() {
        val uuid = UUID.randomUUID()

        val newTask = Task(uuid, "::title::", "::desc::", TaskStatus.NEW, Duration.ofDays(1L))
        val pendingTask = Task(uuid, "::title::", "::desc::", TaskStatus.PENDING, Duration.ofDays(1L))
        val foundProject = Project(uuid, "::title::", "::description::", ProjectStatus.NEW, "::company::", "::client::", listOf(newTask, pendingTask))

        `when`(projectRepository.get(uuid)).thenReturn(Optional.of(foundProject))

        val result = projectService.getProjectById(uuid)

        Assertions.assertTrue(result.isPresent)
        Assertions.assertEquals(result.get(), foundProject)

        verify(projectRepository).get(uuid)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenNoProject_whenGet_thenReturnEmpty() {
        val uuid = UUID.randomUUID()

        `when`(projectRepository.get(uuid)).thenReturn(Optional.empty())

        val result = projectService.getProjectById(uuid)

        Assertions.assertFalse(result.isPresent)

        verify(projectRepository).get(uuid)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenTask_whenUpdateTask_thenUpdateTask() {
        val uuid = UUID.randomUUID()

        val task = Task(uuid, "::title::", "::desc::", TaskStatus.NEW, Duration.ofDays(1L))
        val updatedTask = Task(uuid, "::title::", "::descUpdated::", TaskStatus.NEW, Duration.ofDays(1L))

        `when`(taskRepository.get(uuid)).thenReturn(Optional.of(task))

        projectService.updateTask(UpdateTaskRequest(uuid, "::title::", "::descUpdated::", Duration.ofDays(1L)))

        verify(taskRepository).get(uuid)
        verify(taskRepository).save(updatedTask)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenNoTask_whenUpdateTask_thenWillThrowException() {
        val uuid = UUID.randomUUID()

        `when`(taskRepository.get(uuid)).thenReturn(Optional.empty())

        assertThrows<TaskNotFoundException> {
            projectService.updateTask(UpdateTaskRequest(uuid, "::title::", "::descUpdated::", Duration.ofDays(1L)))

        }

        verify(taskRepository).get(uuid)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenTask_whenUpdateTaskStatusValid_thenUpdateTask() {
        val uuid = UUID.randomUUID()

        val task = Task(uuid, "::title::", "::desc::", TaskStatus.NEW, Duration.ofDays(1L))
        val updatedTask = Task(uuid, "::title::", "::desc::", TaskStatus.PENDING, Duration.ofDays(1L))

        `when`(taskRepository.get(uuid)).thenReturn(Optional.of(task))

        projectService.updateTaskStatus(UpdateTaskStatusRequest(uuid, TaskStatus.PENDING))

        verify(taskRepository).get(uuid)
        verify(taskRepository).save(updatedTask)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenTask_whenUpdateTaskStatusInvalid_thenUpdateTask() {
        val uuid = UUID.randomUUID()

        assertThrows<TaskStatusChangeException> {
            projectService.updateTaskStatus(UpdateTaskStatusRequest(uuid, TaskStatus.DELETED))
        }

        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenNoTask_whenUpdateTaskStatus_thenWillThrowException() {
        val uuid = UUID.randomUUID()

        `when`(taskRepository.get(uuid)).thenReturn(Optional.empty())

        assertThrows<TaskNotFoundException> {
            projectService.updateTaskStatus(UpdateTaskStatusRequest(uuid, TaskStatus.PENDING))

        }

        verify(taskRepository).get(uuid)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenTask_whenDelete_thenMarkAsDeleted() {
        val uuid = UUID.randomUUID()

        val task = Task(uuid, "::title::", "::desc::", TaskStatus.NEW, Duration.ofDays(1L))
        val updatedTask = Task(uuid, "::title::", "::desc::", TaskStatus.DELETED, Duration.ofDays(1L))

        `when`(taskRepository.get(uuid)).thenReturn(Optional.of(task))

        projectService.deleteTask(uuid)

        verify(taskRepository).get(uuid)
        verify(taskRepository).save(updatedTask)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }

    @Test
    fun givenNoTask_whenDelete_thenWillThrowException() {
        val uuid = UUID.randomUUID()

        `when`(taskRepository.get(uuid)).thenReturn(Optional.empty())

        assertThrows<TaskNotFoundException> {
            projectService.deleteTask(uuid)
        }

        verify(taskRepository).get(uuid)
        verifyNoMoreInteractions(taskRepository, projectRepository, idGenerator)
    }
}