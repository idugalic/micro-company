package com.idugalic.commandside.blog.command;

import java.util.Date;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import com.idugalic.common.command.AuditableAbstractCommand;
import com.idugalic.common.model.AuditEntry;

public class PublishBlogPostCommand extends AuditableAbstractCommand {

    @TargetAggregateIdentifier
    private String id;
    private Date publishAt;

    public PublishBlogPostCommand(String id, AuditEntry auditEntry, Date publishAt) {
        this.id = id;
        this.publishAt = publishAt;
        this.setAuditEntry(auditEntry);
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
