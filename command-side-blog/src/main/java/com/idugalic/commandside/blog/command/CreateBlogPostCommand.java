package com.idugalic.commandside.blog.command;

import java.util.Date;
import java.util.UUID;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.idugalic.common.blog.model.BlogPostCategory;
import com.idugalic.common.model.AuditEntry;

public class CreateBlogPostCommand {

	@JsonIgnore
	@TargetAggregateIdentifier
	private String id;
	@NotNull(message = "Title is mandatory")
	@NotBlank(message = "Title is mandatory")
	private String title;
	@NotNull(message = "rawContent is mandatory")
	@NotBlank(message = "rawContent is mandatory")
	private String rawContent;
	@NotNull(message = "PublicSlug is mandatory")
	@NotBlank(message = "PublicSlug is mandatory")
	private String publicSlug;
	@NotNull
	private Boolean draft;
	@NotNull
	private Boolean broadcast;
	@Future(message = "Publish at date must be the future")
    @NotNull
	private Date publishAt;
	@NotNull
	private BlogPostCategory category;

	public CreateBlogPostCommand(AuditEntry auditEntry, String title, String rawContent, String publicSlug,
			Boolean draft, Boolean broadcast, Date publishAt, BlogPostCategory category) {
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
