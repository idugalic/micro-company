package com.idugalic.commandside.project.aggregate;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idugalic.commandside.project.command.CreateProjectCommand;
import com.idugalic.common.project.event.ProjectCreatedEvent;

public class ProjectAggregate extends AbstractAnnotatedAggregateRoot {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2043271011122572733L;


	private static final Logger LOG = LoggerFactory.getLogger(ProjectAggregate.class);
	

	/**
	 * Aggregates that are managed by Axon must have a unique identifier.
	 * Strategies similar to GUID are often used. The annotation
	 * 'AggregateIdentifier' identifies the id field as such.
	 */
	@AggregateIdentifier
	private String id;
	

	/**
	 * This default constructor is used by the Repository to construct a
	 * prototype ProjectAggregate. Events are then used to set properties such
	 * as the ProjectAggregate's Id in order to make the Aggregate reflect it's
	 * true logical state.
	 */
	public ProjectAggregate() {
	}

	/**
	 * This constructor is marked as a 'CommandHandler' for the
	 * AddProductCommand. This command can be used to construct new instances of
	 * the Aggregate. If successful a new ProjectAggregate is 'applied' to the
	 * aggregate using the Axon 'apply' method. The apply method appears to also
	 * propagate the Event to any other registered 'Event Listeners', who may
	 * take further action.
	 *
	 * @param command
	 */
	@CommandHandler
	public ProjectAggregate(CreateProjectCommand command) {
		LOG.debug("Command: 'CreateProjectCommand' received.");
		LOG.debug("Queuing up a new ProjectCreatedEvent for blog post '{}'", command.getId());
		apply(new ProjectCreatedEvent());
	}


	/**
	 * This method is marked as an EventSourcingHandler and is therefore used by
	 * the Axon framework to handle events of the specified type
	 * (BlogPostCreatedEvent). The ProjectCreatedEvent can be raised either by
	 * the constructor during ProjectAggregate(CreateProjectCommand) or by the
	 * Repository when 're-loading' the aggregate.
	 *
	 * @param event
	 */
	@EventSourcingHandler
	public void on(ProjectCreatedEvent event) {
		this.id = event.getId();

		LOG.debug("Applied: 'ProjectCreatedEvent' [{}]", event.getId());
	}


	public static Logger getLog() {
		return LOG;
	}

	public String getId() {
		return id;
	}
	

}
