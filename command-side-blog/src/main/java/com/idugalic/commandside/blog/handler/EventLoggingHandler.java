package com.idugalic.commandside.blog.handler;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.idugalic.common.blog.event.BlogPostCreatedEvent;

/**
 * EventHandler's (a.k.a. EventListeners) are used to react to events and
 * perform associated actions, such as updating a 'materialised-view' for
 * example. Created by ben on 24/09/15.
 */
@Component
public class EventLoggingHandler {

	private static final Logger LOG = LoggerFactory.getLogger(EventLoggingHandler.class);
	private static final String IID = String.valueOf(Double.valueOf(Math.random() * 100).intValue());

	@EventHandler
	public void handle(BlogPostCreatedEvent event) {
		LOG.debug("IID:{} ET:{} EID:[{}]", IID, event.getClass().getSimpleName(), event.getId());
	}

}
