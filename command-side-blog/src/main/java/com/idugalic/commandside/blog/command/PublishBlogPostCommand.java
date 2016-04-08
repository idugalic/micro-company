package com.idugalic.commandside.blog.command;

import java.util.Date;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import com.idugalic.common.command.AuditableAbstractCommand;
import com.idugalic.common.model.AuditEntry;

public class PublishBlogPostCommand extends AuditableAbstractCommand {

	@TargetAggregateIdentifier
	private final String id;
	private final Date publishAt;

	public PublishBlogPostCommand(AuditEntry auditEntry, String id, Date publishAt) {
		super(auditEntry);
		this.id = id;
		this.publishAt = publishAt;
	}

	public String getId() {
		return id;
	}

	public Date getPublishAt() {
		return publishAt;
	}
}
