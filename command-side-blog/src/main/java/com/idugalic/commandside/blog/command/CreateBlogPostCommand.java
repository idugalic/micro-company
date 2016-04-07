package com.idugalic.commandside.blog.command;

import java.util.Date;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import com.idugalic.common.blog.model.BlogPostCategory;
import com.idugalic.common.command.AuditableAbstractCommand;
import com.idugalic.common.model.AuditEntry;

public class CreateBlogPostCommand extends AuditableAbstractCommand{

	@TargetAggregateIdentifier
	private final String id;

	private final String title;
	private final String rawContent;
	private final String publicSlug;
	private final Boolean draft;
	private final Boolean broadcast;
	private final Date publishAt;
	private final BlogPostCategory category;
	

	public CreateBlogPostCommand(String id, AuditEntry auditEntry, String title, String rawContent, String publicSlug, Boolean draft,
			Boolean broadcast, Date publishAt, BlogPostCategory category) {
		super(auditEntry);
		this.id = id;
		this.title = title;
		this.rawContent = rawContent;
		this.publicSlug = publicSlug;
		this.draft = draft;
		this.broadcast = broadcast;
		this.publishAt = publishAt;
		this.category = category;
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
