package com.idugalic.common.blog.event;

import java.util.Date;

import com.idugalic.common.event.AuditableAbstractEvent;
import com.idugalic.common.model.AuditEntry;

public class BlogPostPublishedEvent extends AuditableAbstractEvent {

    private static final long serialVersionUID = 1534382475023480979L;
    private Date publishAt;

    public BlogPostPublishedEvent() {

    }

    public BlogPostPublishedEvent(String id, AuditEntry auditEntry, Date publishAt) {
        super(id, auditEntry);
        this.publishAt = publishAt;
    }

    public Date getPublishAt() {
        return publishAt;
    }

}
