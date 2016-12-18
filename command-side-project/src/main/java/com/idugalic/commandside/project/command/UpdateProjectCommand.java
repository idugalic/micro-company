package com.idugalic.commandside.project.command;

import com.idugalic.common.command.AuditableAbstractCommand;
import com.idugalic.common.model.AuditEntry;

import javax.validation.constraints.NotNull;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.hibernate.validator.constraints.NotBlank;

/**
 * A command for updating the project.
 * 
 * @author idugalic
 *
 */
public class UpdateProjectCommand extends AuditableAbstractCommand {

    @TargetAggregateIdentifier
    private String id;
    @NotNull(message = "Name is mandatory")
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotNull(message = "Repo Url is mandatory")
    @NotBlank(message = "Repo Url is mandatory")
    private String repoUrl;
    private String siteUrl;
    private String description;

    public UpdateProjectCommand(String id, AuditEntry auditEntry, String name, String repoUrl, String siteUrl, String description) {
        super(auditEntry);
        this.id = id;
        this.name = name;
        this.repoUrl = repoUrl;
        this.siteUrl = siteUrl;
        this.description = description;
    }

    public String getId() {
        return id;
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

    public String getDescription() {
        return description;
    }

}
