package com.idugalic.queryside.blog.handler;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.idugalic.common.blog.event.BlogPostCreatedEvent;

@Component
public class EventLoggingHandler {

	private static final Logger LOG = LoggerFactory.getLogger(EventLoggingHandler.class);
	private static final String IID = String.valueOf(Double.valueOf(Math.random() * 100).intValue());

	@EventHandler
	public void handle(BlogPostCreatedEvent event) {
		LOG.debug("IID:{} ET:{} EID:[{}]", IID, event.getClass().getSimpleName(), event.getId());
	}

}
