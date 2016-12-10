package com.idugalic.queryside.blog.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Version;

import com.idugalic.common.blog.event.BlogPostCreatedEvent;
import com.idugalic.common.blog.model.BlogPostCategory;

/**
 * A JPA entity. It represents materialized view of a {@link BlogPostAggregate}
 * 
 * @author idugalic
 *
 */
@Entity
public class BlogPost {
    @Id
    private String id;
    @Version
    private Long version;
    private Long aggregateVersion;
    private String title;
    private String rawContent;
    private String renderContent;
    private String publicSlug;
    private Boolean draft;
    private Boolean broadcast;
    private Date publishAt;
    @Enumerated(EnumType.STRING)
    private BlogPostCategory category;
    private String authorId;

    public BlogPost() {
    }

    public BlogPost(String id, Long aggregateVersion, String title, String rawContent, String renderContent, String publicSlug, Boolean draft,
                    Boolean broadcast, Date publishAt, BlogPostCategory category, String authorId) {
        super();
        this.id = id;
        this.aggregateVersion = aggregateVersion;
        this.title = title;
        this.rawContent = rawContent;
        this.renderContent = renderContent;
        this.publicSlug = publicSlug;
        this.draft = draft;
        this.broadcast = broadcast;
        this.publishAt = publishAt;
        this.category = category;
        this.authorId = authorId;
    }

    public BlogPost(BlogPostCreatedEvent event, Long aggregateVersion) {
        super();
        this.id = event.getId();
        this.aggregateVersion = aggregateVersion;
        this.title = event.getTitle();
        this.rawContent = event.getRawContent();
        // TODO change this
        this.renderContent = event.getRawContent();
        this.publicSlug = event.getPublicSlug();
        this.draft = event.isDraft();
        this.broadcast = event.isBroadcast();
        this.publishAt = event.getPublishAt();
        this.category = event.getCategory();
        this.authorId = event.getAuthorId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRawContent() {
        return rawContent;
    }

    public void setRawContent(String rawContent) {
        this.rawContent = rawContent;
    }

    public String getPublicSlug() {
        return publicSlug;
    }

    public void setPublicSlug(String publicSlug) {
        this.publicSlug = publicSlug;
    }

    public Boolean getDraft() {
        return draft;
    }

    public void setDraft(Boolean draft) {
        this.draft = draft;
    }

    public Boolean getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(Boolean broadcast) {
        this.broadcast = broadcast;
    }

    public Date getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(Date publishAt) {
        this.publishAt = publishAt;
    }

    public BlogPostCategory getCategory() {
        return category;
    }

    public void setCategory(BlogPostCategory category) {
        this.category = category;
    }

    public String getRenderContent() {
        return renderContent;
    }

    public void setRenderContent(String renderContent) {
        this.renderContent = renderContent;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Long getVersion() {
        return aggregateVersion;
    }

    public void setVersion(Long version) {
        this.aggregateVersion = version;
    }

    public Long getAggregateVersion() {
        return aggregateVersion;
    }

    public void setAggregateVersion(Long aggregateVersion) {
        this.aggregateVersion = aggregateVersion;
    }

}
