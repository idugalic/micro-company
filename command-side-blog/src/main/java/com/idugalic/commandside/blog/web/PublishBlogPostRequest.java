package com.idugalic.commandside.blog.web;

import java.util.Date;

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
