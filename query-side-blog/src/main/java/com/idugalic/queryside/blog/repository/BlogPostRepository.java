package com.idugalic.queryside.blog.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.idugalic.queryside.blog.domain.BlogPost;

@RepositoryRestResource(collectionResourceRel = "blogposts", path = "blogposts")
public interface BlogPostRepository extends ReadOnlyPagingAndSortingRepository {
	public List<BlogPost> findByDraft(@Param("draft") Boolean draft);
}
