package com.idugalic.commandside.blog.aggregate;

import java.util.Date;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idugalic.commandside.blog.aggregate.exception.PublishBlogPostException;
import com.idugalic.commandside.blog.command.CreateBlogPostCommand;
import com.idugalic.commandside.blog.command.PublishBlogPostCommand;
import com.idugalic.common.blog.event.BlogPostCreatedEvent;
import com.idugalic.common.blog.event.BlogPostPublishedEvent;
import com.idugalic.common.blog.model.BlogPostCategory;

public class BlogPostAggregate extends AbstractAnnotatedAggregateRoot {

	private static final Logger LOG = LoggerFactory.getLogger(BlogPostAggregate.class);

	/**
	 * Aggregates that are managed by Axon must have a unique identifier.
	 * Strategies similar to GUID are often used. The annotation
	 * 'AggregateIdentifier' identifies the id field as such.
	 */
	@AggregateIdentifier
	private String id;
	private String title;
	private String rawContent;
	private String publicSlug;
	private Boolean draft;
	private Boolean broadcast;
	private Date publishAt;
	private BlogPostCategory category;

	/**
	 * This default constructor is used by the Repository to construct a
	 * prototype BlogPostAggregate. Events are then used to set properties such
	 * as the BlogPostAggregate's Id in order to make the Aggregate reflect it's
	 * true logical state.
	 */
	public BlogPostAggregate() {
	}

	/**
	 * This constructor is marked as a 'CommandHandler' for the
	 * AddProductCommand. This command can be used to construct new instances of
	 * the Aggregate. If successful a new BlogPostAggregate is 'applied' to the
	 * aggregate using the Axon 'apply' method. The apply method appears to also
	 * propagate the Event to any other registered 'Event Listeners', who may
	 * take further action.
	 *
	 * @param command
	 */
	@CommandHandler
	public BlogPostAggregate(CreateBlogPostCommand command) {
		LOG.debug("Command: 'CreateBlogPostCommand' received.");
		LOG.debug("Queuing up a new BlogPostCreatedEvent for blog post '{}'", command.getId());
		apply(new BlogPostCreatedEvent(command.getId(), command.getAuditEntry(), command.getTitle(),
				command.getRawContent(), command.getPublicSlug(), command.getDraft(), command.getBroadcast(),
				command.getPublishAt(), command.getCategory()));
	}

	@CommandHandler
	public void publishBlogPost(PublishBlogPostCommand command) {
		LOG.debug("Command: 'PublishBlogPostCommand' received.");
		if (this.getDraft()) {
			apply(new BlogPostPublishedEvent(id, command.getAuditEntry(), command.getPublishAt()));
		} else {
			throw new PublishBlogPostException("This BlogPostAggregate (" + this.getId() + ") is not a Draft.");
		}
	}

	/**
	 * This method is marked as an EventSourcingHandler and is therefore used by
	 * the Axon framework to handle events of the specified type
	 * (BlogPostCreatedEvent). The BlogPostCreatedEvent can be raised either by
	 * the constructor during BlogPostAggregate(CreateBlogPostCommand) or by the
	 * Repository when 're-loading' the aggregate.
	 *
	 * @param event
	 */
	@EventSourcingHandler
	public void on(BlogPostCreatedEvent event) {
		this.id = event.getId();
		this.title = event.getTitle();
		this.rawContent = event.getRawContent();
		this.publicSlug = event.getPublicSlug();
		this.draft = event.isDraft();
		this.broadcast = event.isBroadcast();
		this.publishAt = event.getPublishAt();
		this.category = event.getCategory();
		LOG.debug("Applied: 'BlogPostCreatedEvent' [{}]", event.getId());
	}

	@EventSourcingHandler
	public void on(BlogPostPublishedEvent event) {
		this.draft = Boolean.FALSE;
		this.publishAt = event.getPublishAt();
		LOG.debug("Applied: 'BlogPostPublishedEvent' [{}]", event.getId());
	}

	public static Logger getLog() {
		return LOG;
	}

	public String getId() {
		return id;
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

	public Boolean getDraft() {
		return draft;
	}

	public Boolean getBroadcast() {
		return broadcast;
	}

	public Date getPublishAt() {
		return publishAt;
	}

	public BlogPostCategory getCategory() {
		return category;
	}

}
