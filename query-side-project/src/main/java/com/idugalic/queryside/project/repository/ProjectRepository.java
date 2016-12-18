package com.idugalic.queryside.project.repository;

import com.idugalic.queryside.project.domain.Project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * A JPA repository for {@link Project}.
 * 
 * @author idugalic
 *
 */
@RepositoryRestResource(collectionResourceRel = "projects", path = "projects")
public interface ProjectRepository extends ReadOnlyPagingAndSortingRepository {

    Page<Project> findByCategory(@Param("category") String category, Pageable pageable);
}
