package com.idugalic.commandside.blog.aggregate;

import java.util.Date;

import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.axonframework.test.matchers.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.idugalic.commandside.blog.command.CreateBlogPostCommand;
import com.idugalic.commandside.blog.command.PublishBlogPostCommand;
import com.idugalic.common.blog.event.BlogPostCreatedEvent;
import com.idugalic.common.blog.model.BlogPostCategory;

public class BlogPostAggregateTest {

	private FixtureConfiguration<BlogPostAggregate> fixture;

	@Before
	public void setUp() throws Exception {
		fixture = Fixtures.newGivenWhenThenFixture(BlogPostAggregate.class);
	}
	
	@Test
    public void testCreateBlogPost() throws Exception {
		CreateBlogPostCommand command = new CreateBlogPostCommand("title", "rowContent" , "publicSlug", Boolean.TRUE, Boolean.FALSE, new Date(), BlogPostCategory.ENGINEERING);
        fixture.given()
                .when(command)
                .expectEventsMatching(Matchers.listWithAllOf(new BlogPostCreatedEventMatcher(command.getId(), command.getTitle(), command.getRawContent() , command.getPublicSlug(), Boolean.TRUE, Boolean.FALSE, command.getPublishAt(), BlogPostCategory.ENGINEERING)));
    }
	
	@Test
    public void testPublishBlogPost() throws Exception {
		PublishBlogPostCommand command = new PublishBlogPostCommand("id", new Date());
        fixture.given(new BlogPostCreatedEvent(command.getId(), null, "title", "rawContent", "publicSlug", Boolean.TRUE, Boolean.TRUE, command.getPublishAt(), BlogPostCategory.ENGINEERING, "idugalic"))
                .when(command)
                .expectEventsMatching(Matchers.listWithAllOf(new BlogPostPublishedEventMatcher(command.getId(), command.getPublishAt())));
    }
	
}
