package com.idugalic.apigateway.handler;

import com.idugalic.common.blog.event.BlogPostCreatedEvent;
import com.idugalic.common.blog.event.BlogPostPublishedEvent;
import com.idugalic.common.event.AuditableAbstractEvent;
import com.idugalic.common.project.event.ProjectCreatedEvent;
import com.idugalic.common.project.event.ProjectUpdatedEvent;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

/**
 * This handler will proxy all events via Websocket to a client applications
 * 
 * @author idugalic
 *
 */
@Component
public class EventLoggingHandler {

    private static final Logger LOG = LoggerFactory.getLogger(EventLoggingHandler.class);
    private static final String IID = String.valueOf(Double.valueOf(Math.random() * 100).intValue());

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private void publish(String username, AuditableAbstractEvent event, String queue) {
        LOG.debug("IID:{} ET:{} EID:[{}] is published via WebSocket", IID, event.getClass().getSimpleName(), event.getId());
        // TODO send messages to users in the future
        // this.messagingTemplate.convertAndSendToUser(username, queue, event);
        this.messagingTemplate.convertAndSend(queue, event);
    }

    @EventHandler
    public void handleBlogCreate(BlogPostCreatedEvent event) {
        publish(event.getAuditEntry().getWho(), event, "/queue/blog");
    }

    @EventHandler
    public void handleBlogPublis(BlogPostPublishedEvent event) {
        publish(event.getAuditEntry().getWho(), event, "/queue/blog");
    }

    @EventHandler
    public void handleProjectCreate(ProjectCreatedEvent event) {
        publish(event.getAuditEntry().getWho(), event, "/queue/project");
    }

    @EventHandler
    public void handleProjectUpdate(ProjectUpdatedEvent event) {
        publish(event.getAuditEntry().getWho(), event, "/queue/project");
    }
}
