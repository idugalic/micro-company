package com.idugalic.commandside.blog.aggregate;

import java.util.Date;

import org.axonframework.domain.GenericDomainEventMessage;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.idugalic.common.blog.event.BlogPostCreatedEvent;
import com.idugalic.common.blog.model.BlogPostCategory;

public class BlogPostCreatedEventMatcher extends BaseMatcher<BlogPostCreatedEvent> {

	private String id;
	private String title;
	private String rawContent;
	private String publicSlug;
	private Boolean draft;
	private Boolean broadcast;
	private Date publishAt;
	private BlogPostCategory category;

	public BlogPostCreatedEventMatcher(String id, String title, String rawContent, String publicSlug, Boolean draft,
			Boolean broadcast, Date publishAt, BlogPostCategory category) {
		super();
		this.id = id;
		this.title = title;
		this.rawContent = rawContent;
		this.publicSlug = publicSlug;
		this.draft = draft;
		this.broadcast = broadcast;
		this.publishAt = publishAt;
		this.category = category;
	}

	@Override
	public boolean matches(Object arg0) {
		BlogPostCreatedEvent arg = ((GenericDomainEventMessage<BlogPostCreatedEvent>) arg0).getPayload();

		if (arg.getId().equals(this.id) && arg.getTitle().equals(this.title) && arg.getRawContent().equals(this.rawContent)
				&& arg.getPublicSlug().equals(this.publicSlug) && arg.isDraft().equals(this.draft)
				&& arg.isBroadcast().equals(this.broadcast) && arg.getPublishAt().equals(this.publishAt)
				&& arg.getCategory().equals(this.category)) {
			return true;
		} else {
			return false;

		}
	}

	@Override
	public void describeTo(Description arg0) {
		// TODO Auto-generated method stub

	}

}
