package com.idugalic.commandside.blog.web;

import java.util.Date;

/**
 * A web request data transfer object for {@link PublishBLogPostCommand}
 * 
 * @author idugalic
 *
 */
public class PublishBlogPostRequest {

    private Date publishAt;

    public PublishBlogPostRequest() {
    }

    public Date getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(Date publishAt) {
        this.publishAt = publishAt;
    }

}
