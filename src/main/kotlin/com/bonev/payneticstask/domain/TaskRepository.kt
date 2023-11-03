package com.bonev.payneticstask.domain

import java.util.*

interface TaskRepository {
    fun save(task: Task): Task
    fun update(task: Task): Task
    fun get(id: UUID): Optional<Task>
}