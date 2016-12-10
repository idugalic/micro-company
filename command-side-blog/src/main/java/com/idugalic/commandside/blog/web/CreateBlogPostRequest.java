package com.idugalic.commandside.blog.web;

import java.util.Date;

import com.idugalic.common.blog.model.BlogPostCategory;

/**
 * A web request data transfer object for {@link CreateBlogPostCommand}
 * 
 * @author idugalic
 *
 */
public class CreateBlogPostRequest {

    private String title;
    private String rawContent;
    private String publicSlug;
    private Boolean draft;
    private Boolean broadcast;
    private Date publishAt;
    private BlogPostCategory category;

    public CreateBlogPostRequest() {
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

}
