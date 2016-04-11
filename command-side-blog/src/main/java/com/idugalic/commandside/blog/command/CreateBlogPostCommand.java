package com.idugalic.commandside.blog.command;

import java.util.Date;
import java.util.UUID;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.idugalic.common.blog.model.BlogPostCategory;
import com.idugalic.common.command.AuditableAbstractCommand;
import com.idugalic.common.model.AuditEntry;

public class CreateBlogPostCommand extends AuditableAbstractCommand {

	@JsonIgnore
	@TargetAggregateIdentifier
	private String id;

	private String title;
	private String rawContent;
	private String publicSlug;
	private Boolean draft;
	private Boolean broadcast;
	private Date publishAt;
	private BlogPostCategory category;

	public CreateBlogPostCommand(AuditEntry auditEntry, String title, String rawContent, String publicSlug,
			Boolean draft, Boolean broadcast, Date publishAt, BlogPostCategory category) {
		super(auditEntry);
		this.id = UUID.randomUUID().toString();
		this.title = title;
		this.rawContent = rawContent;
		this.publicSlug = publicSlug;
		this.draft = draft;
		this.broadcast = broadcast;
		this.publishAt = publishAt;
		this.category = category;
	}

	public CreateBlogPostCommand() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getRawContent() {
		return rawContent;
	}

	public String getPublicSlug() {
		return publicSlug;
	}

	public Boolean getDraft() {
		return draft;
	}

	public Boolean getBroadcast() {
		return broadcast;
	}

	public Date getPublishAt() {
		return publishAt;
	}

	public BlogPostCategory getCategory() {
		return category;
	}

}
