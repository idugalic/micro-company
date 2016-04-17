package com.idugalic.commandside.blog.aggregate;

import java.util.Date;

import org.axonframework.domain.GenericDomainEventMessage;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.idugalic.common.blog.event.BlogPostPublishedEvent;

public class BlogPostPublishedEventMatcher extends BaseMatcher<BlogPostPublishedEvent> {

	private String id;
	private Date publishAt;

	public BlogPostPublishedEventMatcher(String id, Date publishAt) {
		super();
		this.id = id;
		this.publishAt = publishAt;
	}

	@Override
	public boolean matches(Object arg0) {
		BlogPostPublishedEvent arg = ((GenericDomainEventMessage<BlogPostPublishedEvent>) arg0).getPayload();

		if (arg.getId().equals(this.id) && arg.getPublishAt().compareTo(this.publishAt) == 0) {
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
