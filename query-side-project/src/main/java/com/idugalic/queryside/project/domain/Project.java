package com.idugalic.queryside.project.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import com.idugalic.common.project.event.ProjectCreatedEvent;

@Entity
public class Project {
    @Id
    private String id;
    @Version
    private Long version;
    private Long aggregateVersion;
    private String name;
    private String repoUrl;
    private String siteUrl;
    private String category;
    private String description;

    public Project() {
    }

    public Project(ProjectCreatedEvent event, Long aggregateVersion) {
        super();
        this.id = event.getId();
        this.aggregateVersion = aggregateVersion;
        this.name = event.getName();
        this.repoUrl = event.getRepoUrl();
        this.siteUrl = event.getSiteUrl();
        this.category = event.getCategory();
        this.description = event.getDescription();
    }

    public Project(String id, Long aggregateVersion, String name, String repoUrl, String siteUrl,
                   String category, String description) {
        super();
        this.id = id;
        this.aggregateVersion = aggregateVersion;
        this.name = name;
        this.repoUrl = repoUrl;
        this.siteUrl = siteUrl;
        this.category = category;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getAggregateVersion() {
        return aggregateVersion;
    }

    public void setAggregateVersion(Long aggregateVersion) {
        this.aggregateVersion = aggregateVersion;
    }

}
