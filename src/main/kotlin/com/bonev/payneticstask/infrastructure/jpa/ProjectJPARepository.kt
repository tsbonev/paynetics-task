package com.bonev.payneticstask.infrastructure.jpa

import com.bonev.payneticstask.domain.Project
import com.bonev.payneticstask.domain.ProjectRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
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

    override fun getPage(page: Int, pageSize: Int): List<Project> {
        val pageable: Pageable = PageRequest.of(page, pageSize)
        val items: Page<ProjectEntity> = repo.findAll(pageable)
        return items.get().toList().map { it.toDomain() }
    }
}

interface ProjectCRUDRepository : CrudRepository<ProjectEntity, UUID>,  PagingAndSortingRepository<ProjectEntity, UUID>