package com.idugalic.apigateway.handler;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.idugalic.common.blog.event.BlogPostCreatedEvent;
import com.idugalic.common.blog.event.BlogPostPublishedEvent;
import com.idugalic.common.project.event.ProjectCreatedEvent;
import com.idugalic.common.project.event.ProjectUpdatedEvent;
/**
 * TODO This handler will proxy all events via Websocket to a client applications
 * 
 * @author idugalic
 *
 */
@Component
public class EventLoggingHandler {

	private static final Logger LOG = LoggerFactory.getLogger(EventLoggingHandler.class);
	private static final String IID = String.valueOf(Double.valueOf(Math.random() * 100).intValue());

	//TODO STOMP WebSocket
	@EventHandler
	public void handleBlogCreate(BlogPostCreatedEvent event) {
		LOG.debug("IID:{} ET:{} EID:[{}]", IID, event.getClass().getSimpleName(), event.getId());
		System.out.println("############### "+event.getClass().getSimpleName()+event.getId());
	}
	//TODO STOMP WebSocket
	@EventHandler
	public void handleBlogPublis(BlogPostPublishedEvent event) {
		LOG.debug("IID:{} ET:{} EID:[{}]", IID, event.getClass().getSimpleName(), event.getId());
		System.out.println("############### "+event.getClass().getSimpleName()+event.getId());
	}
	//TODO STOMP WebSocket
	@EventHandler
	public void handleProjectCreate(ProjectCreatedEvent event) {
		LOG.debug("IID:{} ET:{} EID:[{}]", IID, event.getClass().getSimpleName(), event.getId());
		System.out.println("############### "+event.getClass().getSimpleName()+event.getId());
	}
	//TODO STOMP WebSocket
	@EventHandler
	public void handleProjectUpdate(ProjectUpdatedEvent event) {
		LOG.debug("IID:{} ET:{} EID:[{}]", IID, event.getClass().getSimpleName(), event.getId());
		System.out.println("############### "+event.getClass().getSimpleName()+event.getId());
	}


}
