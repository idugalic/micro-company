package com.idugalic.commandside.project.aggregate;

import com.idugalic.commandside.project.command.CreateProjectCommand;
import com.idugalic.commandside.project.command.UpdateProjectCommand;
import com.idugalic.common.project.event.ProjectCreatedEvent;
import com.idugalic.common.project.event.ProjectUpdatedEvent;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class ProjectAggregate {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectAggregate.class);

    /**
     * Aggregates that are managed by Axon must have a unique identifier. Strategies
     * similar to GUID are often used. The annotation 'AggregateIdentifier' identifies the
     * id field as such.
     */
    @AggregateIdentifier
    private String id;
    private String name;
    private String repoUrl;
    private String siteUrl;
    private String category;
    private String description;
    private List<ProjectRelease> releaseList = new ArrayList<ProjectRelease>();

    /**
     * This default constructor is used by the Repository to construct a prototype
     * ProjectAggregate. Events are then used to set properties such as the
     * ProjectAggregate's Id in order to make the Aggregate reflect it's true logical
     * state.
     */
    public ProjectAggregate() {
    }

    /**
     * This constructor is marked as a 'CommandHandler' for the AddProductCommand. This
     * command can be used to construct new instances of the Aggregate. If successful a
     * new ProjectAggregate is 'applied' to the aggregate using the Axon 'apply' method.
     * The apply method appears to also propagate the Event to any other registered 'Event
     * Listeners', who may take further action.
     *
     * @param command
     */
    @CommandHandler
    public ProjectAggregate(CreateProjectCommand command) {
        LOG.debug("Command: 'CreateProjectCommand' received.");
        LOG.debug("Queuing up a new ProjectCreatedEvent for project '{}'", command.getId());
        apply(new ProjectCreatedEvent(command.getId(), command.getAuditEntry(), command.getName(), command.getRepoUrl(), command.getSiteUrl(), command.getCategory(), command
                .getDescription()));
    }

    @CommandHandler
    public void updateProject(UpdateProjectCommand command) {
        LOG.debug("Command: 'UpdateProjectCommand' received.");
        LOG.debug("Queuing up a new ProjectUpdatedEvent for project '{}'", command.getId());
        apply(new ProjectUpdatedEvent(command.getId(), command.getAuditEntry(), command.getName(), command.getRepoUrl(), command.getSiteUrl(), command.getDescription()));

    }

    /**
     * This method is marked as an EventSourcingHandler and is therefore used by the Axon
     * framework to handle events of the specified type (BlogPostCreatedEvent). The
     * ProjectCreatedEvent can be raised either by the constructor during
     * ProjectAggregate(CreateProjectCommand) or by the Repository when 're-loading' the
     * aggregate.
     *
     * @param event
     */
    @EventSourcingHandler
    public void on(ProjectCreatedEvent event) {
        this.id = event.getId();
        this.category = event.getCategory();
        this.description = event.getDescription();
        this.name = event.getName();
        this.repoUrl = event.getRepoUrl();
        this.siteUrl = event.getSiteUrl();
        LOG.debug("Applied: 'ProjectCreatedEvent' [{}]", event.getId());
    }

    @EventSourcingHandler
    public void on(ProjectUpdatedEvent event) {
        this.description = event.getDescription();
        this.name = event.getName();
        this.repoUrl = event.getRepoUrl();
        this.siteUrl = event.getSiteUrl();
        LOG.debug("Applied: 'ProjectUpdatedEvent' [{}]", event.getId());
    }

    public static Logger getLog() {
        return LOG;
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

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public List<ProjectRelease> getReleaseList() {
        return releaseList;
    }

}
