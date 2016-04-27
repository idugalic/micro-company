package com.idugalic.queryside.project.repository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "projects", path = "projects")
public interface ProjectRepository extends ReadOnlyPagingAndSortingRepository {
}
