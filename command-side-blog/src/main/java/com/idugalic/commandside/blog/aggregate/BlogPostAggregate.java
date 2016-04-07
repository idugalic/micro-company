package com.idugalic.commandside.blog.aggregate;

import java.util.Date;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idugalic.commandside.blog.command.CreateBlogPostCommand;
import com.idugalic.common.blog.event.BlogPostCreatedEvent;
import com.idugalic.common.blog.model.BlogPostCategory;

public class BlogPostAggregate extends AbstractAnnotatedAggregateRoot  {
	
    private static final Logger LOG = LoggerFactory.getLogger(BlogPostAggregate.class);

	
	/**
     * Aggregates that are managed by Axon must have a unique identifier.
     * Strategies similar to GUID are often used. The annotation 'AggregateIdentifier'
     * identifies the id field as such.
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
     * This default constructor is used by the Repository to construct
     * a prototype BlogPostAggregate. Events are then used to set properties
     * such as the BlogPostAggregate's Id in order to make the Aggregate reflect
     * it's true logical state.
     */	
	public BlogPostAggregate() {
	}
	
	 /**
     * This constructor is marked as a 'CommandHandler' for the
     * AddProductCommand. This command can be used to construct
     * new instances of the Aggregate. If successful a new BlogPostAggregate
     * is 'applied' to the aggregate using the Axon 'apply' method. The apply
     * method appears to also propagate the Event to any other registered
     * 'Event Listeners', who may take further action.
     *
     * @param command
     */
    @CommandHandler
    public BlogPostAggregate(CreateBlogPostCommand command) {
        LOG.debug("Command: 'CreateBlogPostCommand' received.");
        LOG.debug("Queuing up a new BlogPostCreatedEvent for product '{}'", command.getId());
        apply(new BlogPostCreatedEvent(command.getId(), command.getAuditEntry(), command.getTitle(), command.getRawContent(), command.getPublicSlug(), command.getDraft(), command.getBroadcast(), command.getPublishAt(), command.getCategory()));
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
