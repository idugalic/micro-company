package com.idugalic.common.blog.event;

import java.util.Date;

import com.idugalic.common.blog.model.BlogPostCategory;
import com.idugalic.common.event.AuditableAbstractEvent;
import com.idugalic.common.model.AuditEntry;

public class BlogPostCreatedEvent extends AuditableAbstractEvent {

    private static final long serialVersionUID = 1534382475023480978L;

    private String title;
    private String rawContent;
    private String publicSlug;
    private Boolean draft;
    private Boolean broadcast;
    private Date publishAt;
    private BlogPostCategory category;
    private String authorId;

    public BlogPostCreatedEvent() {

    }

    public BlogPostCreatedEvent(String id, AuditEntry auditEntry, String title, String rawContent, String publicSlug, Boolean draft, Boolean broadcast, Date publishAt,
                                BlogPostCategory category, String authorId) {
        super(id, auditEntry);
        this.title = title;
        this.rawContent = rawContent;
        this.publicSlug = publicSlug;
        this.draft = draft;
        this.broadcast = broadcast;
        this.publishAt = publishAt;
        this.category = category;
        this.authorId = authorId;
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

    public Boolean isDraft() {
        return draft;
    }

    public Boolean isBroadcast() {
        return broadcast;
    }

    public Date getPublishAt() {
        return publishAt;
    }

    public BlogPostCategory getCategory() {
        return category;
    }

    public String getAuthorId() {
        return authorId;
    }

}
