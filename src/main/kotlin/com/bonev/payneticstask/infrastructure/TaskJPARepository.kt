package com.bonev.payneticstask.infrastructure

import com.bonev.payneticstask.domain.Task
import com.bonev.payneticstask.domain.TaskRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class TaskJPARepository(private val repo: TaskCRUDRepository) : TaskRepository {
    override fun save(task: Task): Task {
        val entityTask = task.toEntity()

        val dbTask = repo.save(entityTask)

        return dbTask.toDomain()
    }

    override fun get(id: UUID): Optional<Task> {
        val possibleTask = repo.findById(id)

        return possibleTask.map { it.toDomain() }
    }
}

interface TaskCRUDRepository: CrudRepository<TaskEntity, UUID>