package com.idugalic.commandside.blog.command;

import java.util.Date;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import com.idugalic.common.model.AuditEntry;

public class PublishBlogPostCommand{

	@TargetAggregateIdentifier
	private String id;
	private Date publishAt;

	public PublishBlogPostCommand(AuditEntry auditEntry, String id, Date publishAt) {
		this.id = id;
		this.publishAt = publishAt;
	}
	
	public PublishBlogPostCommand() {
		
	}

	public String getId() {
		return id;
	}
	

	public void setId(String id) {
		this.id = id;
	}

	public Date getPublishAt() {
		return publishAt;
	}
}
