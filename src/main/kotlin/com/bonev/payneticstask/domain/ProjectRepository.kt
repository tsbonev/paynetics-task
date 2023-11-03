package com.bonev.payneticstask.domain

import java.util.*

interface ProjectRepository {
    fun save(project: Project): Project

    fun update(project: Project): Project

    fun get(id: UUID): Optional<Project>

    fun getByClient(client: String): List<Project>
    fun getByCompany(company: String): List<Project>
}