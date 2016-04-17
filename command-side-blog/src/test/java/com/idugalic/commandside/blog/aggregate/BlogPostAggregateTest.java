package com.idugalic.commandside.blog.aggregate;

import java.util.Date;

import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import com.idugalic.commandside.blog.command.CreateBlogPostCommand;
import com.idugalic.commandside.blog.command.PublishBlogPostCommand;
import com.idugalic.common.blog.event.BlogPostCreatedEvent;
import com.idugalic.common.blog.event.BlogPostPublishedEvent;
import com.idugalic.common.blog.model.BlogPostCategory;
import com.idugalic.common.model.AuditEntry;

public class BlogPostAggregateTest {

	private FixtureConfiguration<BlogPostAggregate> fixture;
	private AuditEntry auditEntry;
	private static final String WHO = "idugalic";

	@Before
	public void setUp() throws Exception {
		fixture = Fixtures.newGivenWhenThenFixture(BlogPostAggregate.class);
		auditEntry = new AuditEntry(WHO);
	}

	@Test
	public void testCreateBlogPost() throws Exception {
		CreateBlogPostCommand command = new CreateBlogPostCommand(auditEntry, "title", "rowContent", "publicSlug", Boolean.TRUE, Boolean.FALSE, new Date(), BlogPostCategory.ENGINEERING, WHO);
		fixture.
		given().
		when(command).
		expectEvents(new BlogPostCreatedEvent(command.getId(), command.getAuditEntry(), command.getTitle(), command.getRawContent(), command.getPublicSlug(), command.getDraft(), command.getBroadcast(), command.getPublishAt(), BlogPostCategory.ENGINEERING, WHO));
	}

	@Test
	public void testPublishBlogPost() throws Exception {
		PublishBlogPostCommand command = new PublishBlogPostCommand("id", auditEntry, new Date());
		fixture.
		given(new BlogPostCreatedEvent(command.getId(), command.getAuditEntry(), "title", "rawContent", "publicSlug", Boolean.TRUE, Boolean.TRUE, command.getPublishAt(), BlogPostCategory.ENGINEERING, WHO)).
		when(command).
		expectEvents(new BlogPostPublishedEvent("id", auditEntry, command.getPublishAt()));
	}

}
