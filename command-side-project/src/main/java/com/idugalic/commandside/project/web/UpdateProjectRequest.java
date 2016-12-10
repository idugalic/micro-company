package com.idugalic.commandside.project.web;

import com.idugalic.commandside.project.command.UpdateProjectCommand;

/**
 * A web request data transfer object for {@link UpdateProjectCommand}
 * 
 * @author idugalic
 *
 */
public class UpdateProjectRequest {

    private String name;
    private String repoUrl;
    private String siteUrl;
    private String description;

    public UpdateProjectRequest() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
