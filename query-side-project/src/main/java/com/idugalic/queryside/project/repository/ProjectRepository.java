package com.idugalic.queryside.project.repository;

import com.idugalic.queryside.project.domain.Project;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * A JPA repository for {@link Project}.
 * 
 * @author idugalic
 *
 */
@RepositoryRestResource(collectionResourceRel = "projects", path = "projects")
public interface ProjectRepository extends ReadOnlyPagingAndSortingRepository {
}
