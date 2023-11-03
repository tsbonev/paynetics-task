package com.bonev.payneticstask.infrastructure

import com.bonev.payneticstask.domain.Project
import com.bonev.payneticstask.domain.ProjectRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ProjectJPARepository(private val repo: ProjectCRUDRepository) : ProjectRepository {
    override fun save(project: Project): Project {
        val entityProject = project.toEntity()

        val dbProject = repo.save(entityProject)

        return dbProject.toDomain()
    }
    override fun get(id: UUID): Optional<Project> {
        val possibleProject = repo.findById(id)

        return possibleProject.map { it.toDomain() }
    }
}

interface ProjectCRUDRepository : CrudRepository<ProjectEntity, UUID>