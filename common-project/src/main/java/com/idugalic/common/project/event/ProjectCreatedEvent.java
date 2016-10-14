package com.idugalic.common.project.event;

import com.idugalic.common.event.AuditableAbstractEvent;
import com.idugalic.common.model.AuditEntry;

public class ProjectCreatedEvent extends AuditableAbstractEvent {

    private static final long serialVersionUID = 1534382475023480989L;

    private String name;
    private String repoUrl;
    private String siteUrl;
    private String category;
    private String description;

    public ProjectCreatedEvent() {

    }

    public ProjectCreatedEvent(String id, AuditEntry auditEntry, String name, String repoUrl, String siteUrl, String category, String description) {
        super(id, auditEntry);
        this.name = name;
        this.repoUrl = repoUrl;
        this.siteUrl = siteUrl;
        this.category = category;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

}
