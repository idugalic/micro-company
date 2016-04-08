package com.idugalic.queryside.blog.repository;


import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.idugalic.queryside.blog.domain.BlogPost;

@NoRepositoryBean
public interface ReadOnlyPagingAndSortingRepository extends PagingAndSortingRepository<BlogPost, String> {

    @Override
    @SuppressWarnings("unchecked")
    @RestResource(exported = false)
    BlogPost save(BlogPost entity);

    @Override
    @RestResource(exported = false)
    void delete(String aLong);

    @Override
    @RestResource(exported = false)
    void delete(BlogPost entity);
}
