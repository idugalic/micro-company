package com.idugalic.queryside.blog.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.idugalic.queryside.blog.domain.BlogPost;

/**
 * A read only repository interface - save and delete operations will not be exported as a resource
 * 
 * @author idugalic
 *
 */
@NoRepositoryBean
public interface ReadOnlyPagingAndSortingRepository extends PagingAndSortingRepository<BlogPost, String> {

    @Override
    @SuppressWarnings("unchecked")
    @RestResource(exported = false)
    BlogPost save(BlogPost entity);

    @Override
    @RestResource(exported = false)
    void delete(BlogPost entity);
}
