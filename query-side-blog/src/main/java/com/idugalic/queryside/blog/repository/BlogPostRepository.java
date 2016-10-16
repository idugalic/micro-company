package com.idugalic.queryside.blog.repository;

import com.idugalic.common.blog.model.BlogPostCategory;
import com.idugalic.queryside.blog.domain.BlogPost;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

@RepositoryRestResource(collectionResourceRel = "blogposts", path = "blogposts")
public interface BlogPostRepository extends ReadOnlyPagingAndSortingRepository {
    Page<BlogPost> findByDraftTrue(Pageable pageRequest);

    Page<BlogPost> findByCategoryAndDraftFalse(@Param("category") BlogPostCategory category, Pageable pageable);

    Page<BlogPost> findByDraftFalseAndPublishAtBeforeOrderByPublishAtDesc(@Param("publishedBefore") Date publishedBefore, Pageable pageRequest);

    Page<BlogPost> findByCategoryAndDraftFalseAndPublishAtBefore(@Param("category") BlogPostCategory category, @Param("publishedBefore") Date publishedBefore,
                                                                 Pageable pageRequest);

    Page<BlogPost> findByBroadcastAndDraftFalseAndPublishAtBefore(@Param("broadcast") boolean broadcast, @Param("publishedBefore") Date publishedBefore, Pageable pageRequest);

    Page<BlogPost> findByDraftFalseAndPublishAtAfter(@Param("now") Date now, Pageable pageRequest);

    Page<BlogPost> findByDraftFalseAndAuthorIdAndPublishAtBeforeOrderByPublishAtDesc(@Param("authorId") String authorId, @Param("publishedBefore") Date publishedBefore,
                                                                                     Pageable pageRequest);

    @Query("select p from BlogPost p where YEAR(p.publishAt) = ?1 and MONTH(p.publishAt) = ?2 and DAY(p.publishAt) = ?3")
    Page<BlogPost> findByDateYearMonthDay(@Param("year") int year, @Param("month") int month, @Param("day") int day, Pageable pageRequest);

    @Query("select p from BlogPost p where YEAR(p.publishAt) = ?1 and MONTH(p.publishAt) = ?2")
    Page<BlogPost> findByDateYearMonth(@Param("year") int year, @Param("month") int month, Pageable pageRequest);

    @Query("select p from BlogPost p where YEAR(p.publishAt) = ?1")
    Page<BlogPost> findByDateYear(@Param("year") int year, Pageable pageRequest);

    BlogPost findByTitle(@Param("title") String title);

    BlogPost findByPublicSlugAndDraftFalseAndPublishAtBefore(@Param("publicSlug") String publicSlug, @Param("now") Date now);
}
