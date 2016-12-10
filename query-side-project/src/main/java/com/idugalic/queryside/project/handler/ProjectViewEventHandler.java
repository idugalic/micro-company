package com.idugalic.queryside.project.handler;

import com.idugalic.common.project.event.ProjectCreatedEvent;
import com.idugalic.queryside.project.domain.Project;
import com.idugalic.queryside.project.repository.ProjectRepository;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.SequenceNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Event handler for {@link ProjrcttCreatedEvent}
 * 
 * @author idugalic
 *
 */
@ProcessingGroup("default")
@Component
public class ProjectViewEventHandler{

    private static final Logger LOG = LoggerFactory.getLogger(ProjectViewEventHandler.class);

    @Autowired
    private ProjectRepository projectRepository;

    @EventHandler
    public void handle(ProjectCreatedEvent event, @SequenceNumber Long version) {
        LOG.info("ProjectCreatedEvent: [{}] ", event.getId());
        projectRepository.save(new Project(event, version));
    }
}
