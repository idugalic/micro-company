package com.idugalic.commandside.project.handler;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventhandling.annotation.SequenceNumber;
import org.axonframework.eventhandling.annotation.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.idugalic.common.project.event.ProjectCreatedEvent;

/**
 * EventHandler's (a.k.a. EventListeners) are used to react to events and perform
 * associated actions, such as updating a 'materialised-view' for example.
 */
@Component
public class EventLoggingHandler {

    private static final Logger LOG = LoggerFactory.getLogger(EventLoggingHandler.class);
    private static final String IID = String.valueOf(Double.valueOf(Math.random() * 100).intValue());

    @EventHandler
    public void handle(ProjectCreatedEvent event, @SequenceNumber Long version, @Timestamp org.joda.time.DateTime time) {
        LOG.debug("IID:{} ET:{} EID:[{}]", IID, event.getClass().getSimpleName(), event.getId());
        LOG.debug("at {} with version {}", time.toString(), version);
    }

}
