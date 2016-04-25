package com.idugalic.queryside.project.repository;


import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.idugalic.queryside.project.domain.Project;

@NoRepositoryBean
public interface ReadOnlyPagingAndSortingRepository extends PagingAndSortingRepository<Project, String> {

    @Override
    @SuppressWarnings("unchecked")
    @RestResource(exported = false)
    Project save(Project entity);

    @Override
    @RestResource(exported = false)
    void delete(String aLong);

    @Override
    @RestResource(exported = false)
    void delete(Project entity);
}
