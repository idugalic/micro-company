package com.idugalic.queryside.project.repository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "blogposts", path = "blogposts")
public interface ProjectRepository extends ReadOnlyPagingAndSortingRepository {
}
