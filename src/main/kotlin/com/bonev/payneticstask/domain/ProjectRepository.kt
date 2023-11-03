package com.bonev.payneticstask.domain

import java.util.*

interface ProjectRepository {
    fun save(project: Project): Project

    fun get(id: UUID): Optional<Project>
}