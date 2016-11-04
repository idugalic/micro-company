package com.idugalic.commandside.project.handler;

import com.idugalic.common.project.event.ProjectCreatedEvent;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.SequenceNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

/**
 * EventHandler's (a.k.a. EventListeners) are used to react to events and perform
 * associated actions, such as updating a 'materialised-view' for example.
 */
@Component
public class ProjectEventLoggingHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectEventLoggingHandler.class);
    private static final String IID = String.valueOf(Double.valueOf(Math.random() * 100).intValue());

    @EventHandler
    public void handle(ProjectCreatedEvent event, @SequenceNumber Long version) {
        LOG.debug("IID:{} ET:{} EID:[{}]", IID, event.getClass().getSimpleName(), event.getId());
    }

}
