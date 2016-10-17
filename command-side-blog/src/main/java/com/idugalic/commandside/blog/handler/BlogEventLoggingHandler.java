package com.idugalic.commandside.blog.handler;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventhandling.annotation.SequenceNumber;
import org.axonframework.eventhandling.annotation.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.idugalic.common.blog.event.BlogPostCreatedEvent;
import com.idugalic.common.blog.event.BlogPostPublishedEvent;

/**
 * EventHandler's (a.k.a. EventListeners) are used to react to events and perform
 * associated actions, such as updating a 'materialised-view' for example.
 */
@Component
public class BlogEventLoggingHandler {

    private static final Logger LOG = LoggerFactory.getLogger(BlogEventLoggingHandler.class);
    private static final String IID = String.valueOf(Double.valueOf(Math.random() * 100).intValue());

    @EventHandler
    public void handle(BlogPostCreatedEvent event, @SequenceNumber Long version, @Timestamp org.joda.time.DateTime time) {
        LOG.debug("IID:{} ET:{} EID:[{}]", IID, event.getClass().getSimpleName(), event.getId());
        LOG.debug("at {} with version {}", time.toString(), version);
    }

    @EventHandler
    public void handle(BlogPostPublishedEvent event, @SequenceNumber Long version, @Timestamp org.joda.time.DateTime time) {
        LOG.debug("IID:{} ET:{} EID:[{}]", IID, event.getClass().getSimpleName(), event.getId());
        LOG.debug("at {} with version {}", time.toString(), version);
    }
}
